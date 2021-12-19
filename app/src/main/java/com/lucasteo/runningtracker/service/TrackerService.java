package com.lucasteo.runningtracker.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
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

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.lucasteo.runningtracker.R;
import com.lucasteo.runningtracker.view.MainActivity;
import com.lucasteo.runningtracker.model.RTRepository;
import com.lucasteo.runningtracker.model.Track;

import java.util.Date;
import java.util.List;

public class TrackerService extends Service {


    //region VARIABLES
    //--------------------------------------------------------------------------------------------//

    // debug log
    private static final String TAG = "runningTracker";

    // notification
    private static final String NOTIFICATION_CHANNEL_ID = "trackerService";
    private static final int NOTIFICATION_ID = 1;

    // callbacks
    private final RemoteCallbackList<TrackerServiceBinder> remoteCallbackList =
            new RemoteCallbackList<TrackerServiceBinder>();

    // main service
    private RTRepository repository;
    private LocationManager locationManager;
    private TrackerLocationListener locationListener;
    private Status status = Status.STARTED;

    // location calculation
    private Location prevLocation;
    private double distance = 0;

    //--------------------------------------------------------------------------------------------//
    //endregion

    //region LIFE CYCLE METHODS
    //--------------------------------------------------------------------------------------------//

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: Tracker Service");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // return super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "onStartCommand: Tracker Service");

        // setup repository instance
        repository = new RTRepository(getApplication());

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
        Log.d(TAG, "onRebind: Tracker Service");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind: Tracker Service");

        if(status != Status.JUST_STARTED && status != Status.RUNNING){
            stopService();
        }

        return true; // to trigger onRebind
    }

    @Override
    public void onLowMemory() {
        Log.d(TAG, "onLowMemory: Tracker Service");
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        Log.d(TAG, "onTrimMemory: Tracker Service");
        super.onTrimMemory(level);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d(TAG, "onTaskRemoved: Tracker Service");
        super.onTaskRemoved(rootIntent);
    }

    //--------------------------------------------------------------------------------------------//
    //endregion

    //region CALLBACKS & MAIN SERVICE
    //--------------------------------------------------------------------------------------------//

    public enum Status{
        STARTED(1),
        JUST_STARTED(2),
        RUNNING(3),
        PAUSED(4),
        STOPPED(5);

        private final int value;

        Status(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public class TrackerLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

            // ignore calculation and data storing for the first location
            if(status == Status.RUNNING){
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
            } else if (status == Status.JUST_STARTED){
                status = Status.RUNNING;
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
        public void pauseTrackerService(){
            stopListening();
        }
        public void runTrackerService(){
            startListening();
        }
        public boolean getTrackerServiceIsRunning(){
            return isRunning();
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

    // callback loops
    /*
    public void doCallbacks(int progress) {
        final int n = remoteCallbackList.beginBroadcast();
        for (int i=0; i<n; i++) {
            remoteCallbackList.getBroadcastItem(i).callback.TrackerServiceLocationChange(null);
        }
        remoteCallbackList.finishBroadcast();
    }
    */

    @SuppressLint("MissingPermission")
    private void startListening(){

        // TODO fix location Manager not continue listening after minimizing task (google limited this)
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new TrackerLocationListener();

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1, // minimum time interval between updates
                5, // minimum distance between updates, in metres
                locationListener);

        status = Status.JUST_STARTED;
    }

    private void stopListening(){
        if(locationManager != null){
            locationManager.removeUpdates(locationListener);
        }
        locationListener = null;
        locationManager = null;

        status = Status.PAUSED;
    }

    private void stopService(){
        status = Status.STOPPED;
        stopListening();
        removeNotification();
        stopSelf();
    }

    private boolean isRunning(){
        return ((status == Status.RUNNING) || (status == Status.JUST_STARTED));
    }

    //--------------------------------------------------------------------------------------------//
    //endregion

    //region NOTIFICATIONS
    //--------------------------------------------------------------------------------------------//

    @SuppressLint("ObsoleteSdkInt")
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
    }

    public void removeNotification(){
        stopForeground(true);
    }

    //--------------------------------------------------------------------------------------------//
    //endregion

}
