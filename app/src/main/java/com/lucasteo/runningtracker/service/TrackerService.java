package com.lucasteo.runningtracker.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteCallbackList;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.lucasteo.runningtracker.model.RTRepository;
import com.lucasteo.runningtracker.model.Track;

import java.util.List;

public class TrackerService extends Service {

    // debug log
    private static final String TAG = "TrackerService";

    // notification
    private static final String NOTIFICATION_CHANNEL_ID = "trackerService";
    private static final int NOTIFICATION_ID = 1;

    // callbacks
    private RemoteCallbackList<TrackerServiceBinder> remoteCallbackList =
            new RemoteCallbackList<TrackerServiceBinder>();

    // main service
    private RTRepository repository;
    private LiveData<List<Track>> allTracks;
    private LocationManager locationManager;
    private TrackerLocationListener locationListener;

    //region LIFE CYCLE METHODS
    //--------------------------------------------------------------------------------------------//

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: Tracker Service");

        // setup repository instance
        repository = new RTRepository(getApplication());
        allTracks = repository.getAllTracks();

        // main job of this service
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationListener = new TrackerLocationListener();

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    5, // minimum time interval between updates
                    5, // minimum distance between updates, in metres
                    locationListener);
        } catch(SecurityException e) {
            Log.d(TAG, e.toString());
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: Tracker Service");
        locationManager.removeUpdates(locationListener);
        locationListener = null;
        locationManager = null;
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
    public int onStartCommand(Intent intent, int flags, int startId) {
        // return super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "onStartCommand: Tracker Service");
        return Service.START_STICKY;
    }

    //--------------------------------------------------------------------------------------------//
    //endregion

    //region CALLBACKS & MAIN SERVICE
    //--------------------------------------------------------------------------------------------//

    public class TrackerLocationListener implements LocationListener {

        // TODO do all the callbacks in here
        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, location.getLatitude() + " " + location.getLongitude());
            // int distance = prevLocation.distanceTo(location);
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // information about the signal, i.e. number of satellites
            Log.d(TAG, "onStatusChanged: " + provider + " " + status);
        }
        @Override
        public void onProviderEnabled(String provider) {
            // the user enabled (for example) the GPS
            Log.d(TAG, "onProviderEnabled: " + provider);
        }
        @Override
        public void onProviderDisabled(String provider) {
            // the user disabled (for example) the GPS
            Log.d(TAG, "onProviderDisabled: " + provider);
        }
    }

    public class TrackerServiceBinder extends Binder implements IInterface {
        @Override
        public IBinder asBinder() {
            return this;
        }


        // interact with service through binder

//        public MP3PlayerState getState(){
//            return PlayerService.this.getState();
//        }
//        public void load(String uri){
//            PlayerService.this.load(uri);
//        }
//        public String getFilePath(){
//            return PlayerService.this.getFilePath();
//        }
//        public int getProgress(){
//            return PlayerService.this.getProgress();
//        }
//        public int getDuration(){
//            return PlayerService.this.getDuration();
//        }
//        public void play(){
//            PlayerService.this.play();
//        }
//        public void pause(){
//            PlayerService.this.pause();
//        }
//        public void stop(){
//            PlayerService.this.stop();
//        }
//        public void setSeekTo(int progress){
//            PlayerService.this.setSeekTo(progress);
//        }

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
    //--------------------------------------------------------------------------------------------//
    //endregion



}
