package com.lucasteo.runningtracker.view_model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.lucasteo.runningtracker.model.pojo.GroupByDateTrackPojo;
import com.lucasteo.runningtracker.model.RTRepository;
import com.lucasteo.runningtracker.model.entity.Track;
import com.lucasteo.runningtracker.calculation.SpeedStatus;

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
    private static final String SAVED_KEY_STOP_MOVING = "stopMoving";

    // repo
    private RTRepository repository;
    private final LiveData<List<Track>> allTracks;
    private final LiveData<Track> lastTrack;
    private final LiveData<List<GroupByDateTrackPojo>> allGroupByDateTracks;

    // UI states
    private MutableLiveData<Boolean> serviceStatus;
    private MutableLiveData<Boolean> stopMoving;

    //--------------------------------------------------------------------------------------------//
    //endregion

    //region MAIN CONSTRUCTOR AND METHODS
    //--------------------------------------------------------------------------------------------//

    /**
     * main view model constructor
     *
     * @param application
     * @param savedStateHandle
     */
    public MainViewModel(@NonNull Application application, SavedStateHandle savedStateHandle) {
        super(application);
        Log.d(TAG, "MainViewModel: Instantiated");

        // save instance state
        savedState = savedStateHandle;

        // init variables default
        serviceStatus = new MutableLiveData<>(Boolean.FALSE);
        stopMoving = new MutableLiveData<>(Boolean.TRUE);

        // retrieve saved instance state
        if (savedStateHandle.contains(SAVED_KEY_SERVICE_STARTED)){
            serviceStatus.setValue(savedStateHandle.get(SAVED_KEY_SERVICE_STARTED));
        }
        if (savedStateHandle.contains(SAVED_KEY_STOP_MOVING)){
            stopMoving.setValue(savedStateHandle.get(SAVED_KEY_STOP_MOVING));
        }

        // retrieve data from repository
        repository = new RTRepository(application);
        allTracks = repository.getAllTracks();
        lastTrack = repository.getLastTrack();
        allGroupByDateTracks = repository.getAllGroupByDateTracks();

    }

    //--------------------------------------------------------------------------------------------//
    //endregion

    //region interaction between vm and repo
    //--------------------------------------------------------------------------------------------//

    public LiveData<List<Track>> getAllTracks() {
        return allTracks;
    }

    public LiveData<List<GroupByDateTrackPojo>> getAllGroupByDayTrack(){
        return allGroupByDateTracks;
    }

    public LiveData<Track> getLastTrack(){
        return lastTrack;
    }

    public void deleteAll(){
        repository.deleteAll();
    }

    //--------------------------------------------------------------------------------------------//
    //endregion

    //region VARIABLES STATES GETTER SETTER
    //--------------------------------------------------------------------------------------------//

    // service status getter setter

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

    /**
     * flip service status
     * this is part of the UI state
     */
    public void toggleServiceStatus(){
        boolean value = serviceStatus.getValue() != null ? serviceStatus.getValue() : false;
        setValueServiceStatus(!value);
    }

    // stop moving getter setter

    public MutableLiveData<Boolean> getStopMoving(){
        return stopMoving;
    }

    public void setValueStopMoving(boolean value){
        stopMoving.setValue(value);
        savedState.set(SAVED_KEY_STOP_MOVING, value);
    }

    public boolean getValueStopMoving(){
        return stopMoving.getValue();
    }

    //--------------------------------------------------------------------------------------------//
    //endregion

}

