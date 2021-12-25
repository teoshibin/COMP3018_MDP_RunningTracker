package com.lucasteo.runningtracker.service;

import android.Manifest;
import android.annotation.SuppressLint;
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

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.lucasteo.runningtracker.R;
import com.lucasteo.runningtracker.calculation.SpeedStatus;
import com.lucasteo.runningtracker.view.MainActivity;
import com.lucasteo.runningtracker.model.RTRepository;
import com.lucasteo.runningtracker.model.entity.Track;

import java.util.Date;

/**
 * gps tracker service
 * tracks location, speed, etc. and store these information into database
 */
public class TrackerService extends Service {

    //==============================================================================================
    // variables
    //==============================================================================================

    //region

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
    private boolean interupted = false; // turn into true when user turn off gps

    // location calculation
    private Location prevLocation;
    private double distance = 0;
    private SpeedStatus speedStatus = null;

    /// standing detection (thread stuff)
    // mutually altered boolean (location listener set it to false, standing detection thread set it to true)
    private boolean flipFlop = true;
    // to turn on and off standing detection thread
    private boolean detecting = true;
    // time delay count for one complete detection
    private final int defaultCount = 40;
    // time delay count that is being decremented
    private int count = defaultCount;
    // actual output of standing detection
    private boolean stopMoving = false;

    //endregion

    //==============================================================================================
    // life cycle
    //==============================================================================================

    //region

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
        if (serviceStatus != ServiceStatus.JUST_STARTED && serviceStatus != ServiceStatus.RUNNING) {
            stopService();
        }

        return true; // to trigger onRebind
    }

    //endregion

    //==============================================================================================
    // callbacks and main service
    //==============================================================================================

    //region

    /**
     * define this service status
     */
    public enum ServiceStatus {
        STARTED,
        JUST_STARTED,
        RUNNING,
        PAUSED,
        STOPPED
    }

    /**
     * define gps location listener
     * main service logic
     */
    public class TrackerLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

            // service is fully running then start storing data
            if (serviceStatus == ServiceStatus.RUNNING) {

                if (prevLocation != null) {
                    distance = prevLocation.distanceTo(location);
                }

                // detect user stop moving
                resetStopMovingDetection();

                Log.d(TAG, "onLocationChanged: Moving");

                speedStatus = SpeedStatus.classifyWithScaledThreshold(location.getSpeed());
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

                // ignore calculation and data storing for the first location
                // as distance require 2 locations to be calculated at the beginning
            } else if (serviceStatus == ServiceStatus.JUST_STARTED) {
                serviceStatus = ServiceStatus.RUNNING;
            }
            prevLocation = location; // store current location as previous location

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
            if (interupted && serviceStatus == ServiceStatus.PAUSED) {
                startListening();
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            // the user disabled (for example) the GPS
            Log.d(TAG, "onProviderDisabled: ( Provider: " + provider + " )");
            if (isRunning()) {
                stopListening();
                interupted = true;
            }
        }

    }

    /**
     * define binder for other component to interact with this service
     */
    public class TrackerServiceBinder extends Binder implements IInterface {

        @Override
        public IBinder asBinder() {
            return this;
        }

        // interact with service through binder
        public void pauseTrackerService() {
            stopListening();
        }

        public void runTrackerService() {
            startListening();
        }

        public boolean getTrackerServiceIsRunning() {
            return isRunning();
        }

        public boolean getTrackerServiceStopMoving() {
            return getStopMoving();
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

    // CALLBACKS

    /**
     * do call backs
     */
    public void doStopMovingEventCallback() {
        final int n = remoteCallbackList.beginBroadcast();
        for (int i = 0; i < n; i++) {
            remoteCallbackList.getBroadcastItem(i).callback.onStopMovingUpdateEvent(stopMoving);
        }
        remoteCallbackList.finishBroadcast();
    }

    public void doOnPermissionNotGrantedEventCallback() {
        final int n = remoteCallbackList.beginBroadcast();
        for (int i = 0; i < n; i++) {
            remoteCallbackList.getBroadcastItem(i).callback.onPermissionNotGranted();
        }
        remoteCallbackList.finishBroadcast();
    }

    // STOP MOVING DETECTION METHODS

    /**
     * location listener uses this to reset the standing detection
     * and allow it to continue detecting if user stops moving
     */
    private void resetStopMovingDetection() {
        count = defaultCount;
        flipFlop = false;
        if (stopMoving) {
            stopMoving = false;
            doStopMovingEventCallback();
        }
    }

    /**
     * halt stop moving detection thread
     */
    private void stopStopMovingDetection() {
        detecting = false;
    }

    // SERVICE MAIN METHODS

    /**
     * start running this service
     */
    private void startListening() {

        // if for some god knows what reason that this service is started without permission then do a callback back to GUI
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            doOnPermissionNotGrantedEventCallback();
            return;
        }

        // start the main part of this service
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

                    // reset value at the beginning
                    detecting = true;
                    stopMoving = false;

                    while(detecting){

                        // location listener will set this to false on every location update
                        flipFlop = true;
                        // location listener will reset this to default count on every location update
                        count = defaultCount;

                        // if delay count is positive and still detecting then sleep
                        while(detecting && count > 0){
                            Thread.sleep(250);
                            count -= 1;
                        }

                        // if flip flop is not set to false by location listener then
                        // the user must have stop moving for over 250 ms * 40 count = 10 seconds
                        //     EXTRA NOTE:
                        //         do callback even when the stopMoving status is the same
                        //         if this part of the code is reached bcs of detecting = false then
                        //             abort call back as the detection thread is closed
                        if (detecting && flipFlop){
                            stopMoving = true;
                            doStopMovingEventCallback();
                        }

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // update service status
        serviceStatus = ServiceStatus.JUST_STARTED;
    }

    /**
     * stop listening to location
     */
    private void stopListening(){
        if(locationManager != null){
            locationManager.removeUpdates(locationListener);
        }
        locationListener = null;
        locationManager = null;
        prevLocation = null;

        serviceStatus = ServiceStatus.PAUSED;
        speedStatus = null;

        // detecting standing
        stopStopMovingDetection();
    }

    /**
     * stop entire service
     */
    private void stopService(){
        serviceStatus = ServiceStatus.STOPPED;
        stopListening();
        removeNotification();
        stopSelf();
    }

    /**
     *  check if service is running
     *  this is required to allow GUI to show current service status
     *
     * @return returns true if service is just started running or fully running
     */
    private boolean isRunning(){
        return ((serviceStatus == ServiceStatus.RUNNING) || (serviceStatus == ServiceStatus.JUST_STARTED));
    }

    /**
     * getter for stop moving status
     * this is required to allow GUI to show current motion status
     *
     * @return true if user stop moving
     */
    private boolean getStopMoving(){
        return stopMoving;
    }

    //endregion

    //==============================================================================================
    // notification
    //==============================================================================================

    //region

    /**
     * create foreground notification
     */
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

    /**
     * remove foreground notification
     */
    public void removeNotification(){
        stopForeground(true);
    }

    //endregion

}
