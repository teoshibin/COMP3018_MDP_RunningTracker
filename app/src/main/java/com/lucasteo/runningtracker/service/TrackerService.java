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

import com.lucasteo.runningtracker.R;
import com.lucasteo.runningtracker.calculation.SpeedStatus;
import com.lucasteo.runningtracker.view.MainActivity;
import com.lucasteo.runningtracker.model.RTRepository;
import com.lucasteo.runningtracker.model.entity.Track;

import java.util.Date;

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
    private ServiceStatus serviceStatus = ServiceStatus.STARTED;

    // location calculation
    private Location prevLocation;
    private double distance = 0;
    private SpeedStatus speedStatus = null;

    // standing detection (thread stuff)
    private boolean stopMoving = true;
    private boolean detecting = true;
    private final int defaultCount = 40;
    private int count = defaultCount;

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

        Log.d(TAG, "onStartCommand: Tracker Service");

        // setup repository instance
        repository = new RTRepository(getApplication());

        // start foreground notification
        createNotification();

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: Tracker Service");
        removeNotification(); // when the service is fully removed, remove foreground notification
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

        // stop this service if it is not running on unbind
        if(serviceStatus != ServiceStatus.JUST_STARTED && serviceStatus != ServiceStatus.RUNNING){
            stopService();
        }

        return true; // to trigger onRebind
    }

    //--------------------------------------------------------------------------------------------//
    //endregion

    //region CALLBACKS & MAIN SERVICE
    //--------------------------------------------------------------------------------------------//

    public enum ServiceStatus {
        STARTED(1),
        JUST_STARTED(2),
        RUNNING(3),
        PAUSED(4),
        STOPPED(5);

        private final int value;

        ServiceStatus(int value) {
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
            if(serviceStatus == ServiceStatus.RUNNING){

                if (prevLocation != null){
                    distance = prevLocation.distanceTo(location);
                }

                // detect standing
                count = defaultCount;
                stopMoving = false;

                Log.d(TAG, "onLocationChanged: Moving");
//                Log.d(TAG, "onLocationChanged: \n" +
//                        "\tLatitude: " + location.getLatitude() + "\n" +
//                        "\tLongitude: " + location.getLongitude() + "\n" +
//                        "\tDistance: " + distance + "\n" +
//                        "\tAltitude: " + location.getAltitude() + "\n" +
//                        "\tSpeed (m/s): " + location.getSpeed() + "\n"
//                );

                SpeedStatus newSpeedStatus = SpeedStatus.classifyWithScaledThreshold(location.getSpeed());

                if (newSpeedStatus != speedStatus){
                    speedStatus = newSpeedStatus;
                    doSpeedStatusUpdateCallback(speedStatus);
                }

                repository.insert(
                        new Track(
                                0,
                                location.getLatitude(),
                                location.getLongitude(),
                                distance,
                                location.getAltitude(),
                                location.getSpeed(),
                                speedStatus.name(),
                                new Date()
                        )
                );



            } else if (serviceStatus == ServiceStatus.JUST_STARTED){
                serviceStatus = ServiceStatus.RUNNING;
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
    public void doSpeedStatusUpdateCallback(SpeedStatus status) {
        final int n = remoteCallbackList.beginBroadcast();
        for (int i=0; i<n; i++) {
            remoteCallbackList.getBroadcastItem(i).callback.speedStatusUpdate(status);
        }
        remoteCallbackList.finishBroadcast();
    }

    @SuppressLint("MissingPermission")
    private void startListening(){

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new TrackerLocationListener();

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1, // minimum time interval between updates
                1, // minimum distance between updates, in metres
                locationListener);

        // detect standing if not moving for certain amount of time
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    // reset detecting to true at the beginning
                    detecting = true;
                    while(detecting){

                        // location listener will set this to false on every location update
                        stopMoving = true;
                        // location listener will reset this to default count on every location update
                        count = defaultCount;

                        // if delay count is positive and still detecting then sleep
                        while(count > 0 && detecting){
                            Thread.sleep(250);
                            count -= 1;
                        }

                        // if stop moving is not set to false by location listener then
                        // the user must have stop moving over 250 ms * 40 count = 10 seconds
                        //     EXTRA NOTE:
                        //         only update when the status is different
                        //         if this part of the code is reached bcs of detecting = false then
                        //         abort checking as the detection is closed
                        if (stopMoving && speedStatus != SpeedStatus.STANDING && detecting){
                            speedStatus = SpeedStatus.STANDING;
                            doSpeedStatusUpdateCallback(speedStatus);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        serviceStatus = ServiceStatus.JUST_STARTED;
    }

    private void stopListening(){
        if(locationManager != null){
            locationManager.removeUpdates(locationListener);
        }
        locationListener = null;
        locationManager = null;

        serviceStatus = ServiceStatus.PAUSED;
        speedStatus = null;

        // detecting standing
        detecting = false;
    }

    private void stopService(){
        serviceStatus = ServiceStatus.STOPPED;
        stopListening();
        removeNotification();
        stopSelf();
    }

    private boolean isRunning(){
        return ((serviceStatus == ServiceStatus.RUNNING) || (serviceStatus == ServiceStatus.JUST_STARTED));
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
