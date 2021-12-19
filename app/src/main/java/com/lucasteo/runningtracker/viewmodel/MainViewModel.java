package com.lucasteo.runningtracker.viewmodel;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.lucasteo.runningtracker.model.RTRepository;
import com.lucasteo.runningtracker.model.Track;
import com.lucasteo.runningtracker.service.ICallback;
import com.lucasteo.runningtracker.service.TrackerService;

import java.util.List;

/**
 * main view model for storing temporary state
 */
public class MainViewModel extends AndroidViewModel {

    //region VARIABLES
    //--------------------------------------------------------------------------------------------//
    // save instance state
    SavedStateHandle savedState;

    // debug log
    private static final String TAG = "runningTracker";

    // save instance state value keys
    private static final String SAVED_KEY_SERVICE_STARTED = "serviceStarted";

    // service
    private TrackerService.TrackerServiceBinder trackerServiceBinder;

    // repo
    private RTRepository repository;
    private final LiveData<List<Track>> allTracks;

    // UI states
    private MutableLiveData<Boolean> serviceStatus;

    //--------------------------------------------------------------------------------------------//
    //endregion

    //region MAIN CONSTRUCTOR AND METHODS
    //--------------------------------------------------------------------------------------------//
    public MainViewModel(@NonNull Application application, SavedStateHandle savedStateHandle) {
        super(application);
        Log.d(TAG, "MainViewModel: Instantiated");

        // save instance state
        savedState = savedStateHandle;

        // init variables default
        serviceStatus = new MutableLiveData<>(Boolean.FALSE);

        // retrieve saved instance state
        if (savedStateHandle.contains(SAVED_KEY_SERVICE_STARTED)){
            serviceStatus.setValue(savedStateHandle.get(SAVED_KEY_SERVICE_STARTED));
        }

        // repo stuff
        repository = new RTRepository(application);
        allTracks = repository.getAllTracks();

//        startTrackerService();
    }

    public void toggleServiceStatus(){
        boolean value = serviceStatus.getValue() != null ? serviceStatus.getValue() : false;
//        if(value){
//            stopTrackerService();
//        } else {
//            startTrackerService();
//        }
        setValueServiceStatus(!value);
    }
    //--------------------------------------------------------------------------------------------//
    //endregion

    //region SERVICE
    //--------------------------------------------------------------------------------------------//
//    private void startTrackerService(){
//        // normal code to start service in activity
//        // this.startForegroundService(new Intent(this, TrackerService.class));
//        // this.bindService(new Intent(this, TrackerService.class),
//        //         serviceConnection, Context.BIND_AUTO_CREATE);
//
//        // start service in view model
//        Application applicationRef = getApplication();
//
//        applicationRef.startForegroundService(
//                new Intent(applicationRef, TrackerService.class));
//        applicationRef.bindService(
//                new Intent(applicationRef, TrackerService.class),
//                serviceConnection, Context.BIND_AUTO_CREATE);
//    }
//
//    private void stopTrackerService(){
//        trackerServiceBinder.stopTrackerService();
//    }
//
//    ICallback callback = new ICallback() {
//        // to use this remember to use runOnUiThread new Runnable()
//    };
//
//    private final ServiceConnection serviceConnection = new ServiceConnection() {
//
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder binder) {
//            Log.d(TAG, "onServiceConnected: MainViewModel");
//            trackerServiceBinder = (TrackerService.TrackerServiceBinder) binder;
//            trackerServiceBinder.registerCallback(callback);
//            setValueServiceStatus(true); // as service existed change status to started
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            Log.d(TAG, "onServiceDisconnected: MainActivity");
//            trackerServiceBinder.unregisterCallback(callback);
//            trackerServiceBinder = null;
//        }
//    };
    //--------------------------------------------------------------------------------------------//
    //endregion

    //region VM and Repo Interaction
    //--------------------------------------------------------------------------------------------//
    public void insert(Track track) {
        repository.insert(track);
    }

    public LiveData<List<Track>> getAllTracks() {
        return allTracks;
    }
    //--------------------------------------------------------------------------------------------//
    //endregion

    //region VARIABLES STATES GETTER SETTER
    //--------------------------------------------------------------------------------------------//
    public MutableLiveData<Boolean> getServiceStatus() {
        return serviceStatus;
    }
    public void setValueServiceStatus(boolean value){
        serviceStatus.setValue(value);
        savedState.set(SAVED_KEY_SERVICE_STARTED, value);
    }

    public boolean getValueServiceStatus(){
        return serviceStatus.getValue() != null ? serviceStatus.getValue() : false;
    }

    //--------------------------------------------------------------------------------------------//
    //endregion

}

