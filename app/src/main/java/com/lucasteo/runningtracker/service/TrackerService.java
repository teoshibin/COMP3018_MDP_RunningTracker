package com.lucasteo.runningtracker.service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteCallbackList;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LiveData;

import com.lucasteo.runningtracker.R;
import com.lucasteo.runningtracker.view.MainActivity;
import com.lucasteo.runningtracker.model.RTRepository;
import com.lucasteo.runningtracker.model.Track;

import java.util.Date;
import java.util.List;

public class TrackerService extends Service {

    // debug log
    private static final String TAG = "TrackerService";

    // notification
    private static final String NOTIFICATION_CHANNEL_ID = "trackerService";
    private static final int NOTIFICATION_ID = 1;
    private NotificationManager notificationManager;

    // callbacks
    private RemoteCallbackList<TrackerServiceBinder> remoteCallbackList =
            new RemoteCallbackList<TrackerServiceBinder>();

    // main service
    private RTRepository repository;
    private LiveData<List<Track>> allTracks;
    private LocationManager locationManager;
    private TrackerLocationListener locationListener;
    private boolean justStarted = true;

    // location calculation
    private Location prevLocation;
    private double distance = 0;

    //region LIFE CYCLE METHODS
    //--------------------------------------------------------------------------------------------//

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: Tracker Service");

        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // return super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "onStartCommand: Tracker Service");

        // TODO fix service not continuing after minimizing task

        // setup repository instance
        repository = new RTRepository(getApplication());
        allTracks = repository.getAllTracks();

        // main job of this service
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new TrackerLocationListener();

        // block service from continuing if permission is not granted, restart service later
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // all permissions are all requested at once at the beginning of the app start in MainActivity
            return Service.START_STICKY;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1, // minimum time interval between updates
                5, // minimum distance between updates, in metres
                locationListener);

        createNotification();

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: Tracker Service");
        removeNotification();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: Tracker Service");
        return new TrackerServiceBinder();
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d(TAG, "onRebind: Tracker Service");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind: Tracker Service");
        return super.onUnbind(intent);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.d(TAG, "onLowMemory: Tracker Service");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Log.d(TAG, "onTrimMemory: Tracker Service");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.d(TAG, "onTaskRemoved: Tracker Service");
    }

    //--------------------------------------------------------------------------------------------//
    //endregion

    //region CALLBACKS & MAIN SERVICE
    //--------------------------------------------------------------------------------------------//

    public class TrackerLocationListener implements LocationListener {

        // TODO do all the callbacks in here
        @Override
        public void onLocationChanged(Location location) {

            // ignore calculation and data storing for the first location
            if(!justStarted){
                if (prevLocation != null){
                    distance = prevLocation.distanceTo(location);
                }
                Log.d(TAG, "onLocationChanged: \n" +
                        "\tLatitude: " + location.getLatitude() + "\n" +
                        "\tLongitude: " + location.getLongitude() + "\n" +
                        "\tDistance: " + distance + "\n" +
                        "\tAltitude: " + location.getAltitude() + "\n" +
                        "\tSpeed (m/s): " + location.getSpeed() + "\n"
                );
                repository.insert(
                        new Track(
                                0,
                                location.getLatitude(),
                                location.getLongitude(),
                                distance,
                                location.getAltitude(),
                                location.getSpeed(),
                                new Date()
                        )
                );
            } else {
                justStarted = false;
            }
            prevLocation = location; // init or continue storing previous location

        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // information about the signal, i.e. number of satellites
            Log.d(TAG, "onStatusChanged: ( Provider: " + provider + " Status: " + status + " )");
        }
        @Override
        public void onProviderEnabled(String provider) {
            // the user enabled (for example) the GPS
            Log.d(TAG, "onProviderEnabled: ( Provider: " + provider + " )");
        }
        @Override
        public void onProviderDisabled(String provider) {
            // the user disabled (for example) the GPS
            Log.d(TAG, "onProviderDisabled: ( Provider: " + provider + " )");
        }
    }

    public class TrackerServiceBinder extends Binder implements IInterface {
        @Override
        public IBinder asBinder() {
            return this;
        }

        // interact with service through binder

        public void stopTrackerService(){
            stopService();
        }

        // callback methods
        public void registerCallback(ICallback callback) {
            this.callback = callback;
            remoteCallbackList.register(TrackerServiceBinder.this);
        }

        public void unregisterCallback(ICallback callback) {
            remoteCallbackList.unregister(TrackerServiceBinder.this);
        }

        ICallback callback;
    }

    public void doCallbacks(int progress) {
//        final int n = remoteCallbackList.beginBroadcast();
//        for (int i=0; i<n; i++) {
//            remoteCallbackList.getBroadcastItem(i).callback.TrackerServiceLocationChange(null);
//            // TODO callback loops
//        }
//        remoteCallbackList.finishBroadcast();
    }

    public void stopService(){
        if(locationManager != null){
            locationManager.removeUpdates(locationListener);
        }
        locationListener = null;
        locationManager = null;
        removeNotification();
        stopSelf();
    }

    //--------------------------------------------------------------------------------------------//
    //endregion

    //region NOTIFICATIONS
    public void createNotification(){

        // create channel if version >= oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setSound(null, null);
            channel.setShowBadge(false);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, 0);

        CharSequence text = getText(R.string.notification_text);

        Notification notification =
                new Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_baseline_directions_run_24)
                        .setTicker(text) // a small line of text on top of the screen
                        .setContentTitle(getText(R.string.notification_title))
                        .setContentText(text)
                        .setContentIntent(pendingIntent)
                        .build();

        // Notification ID cannot be 0.
        startForeground(NOTIFICATION_ID, notification);
//        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    public void removeNotification(){
//        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(TrackerService.this);
//        managerCompat.cancelAll();
//        notificationManager.cancel(NOTIFICATION_ID);
        stopForeground(true);
    }
    //endregion

}
