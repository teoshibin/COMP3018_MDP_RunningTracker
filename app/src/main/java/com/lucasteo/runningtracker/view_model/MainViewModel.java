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
    private static final String SAVED_KEY_SPEED_STATUS = "speedStatus";
    private static final String SAVED_KEY_STOP_MOVING = "stopMoving";

    // repo
    private RTRepository repository;
    private final LiveData<List<Track>> allTracks;
    private final LiveData<Track> lastTrack;
    private final LiveData<List<GroupByDateTrackPojo>> allGroupByDateTracks;

    // UI states
    private MutableLiveData<Boolean> serviceStatus;
//    private MutableLiveData<SpeedStatus> speedStatus;
    private MutableLiveData<Boolean> stopMoving;
    private boolean justStarted = true;

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
        stopMoving = new MutableLiveData<>(Boolean.TRUE);
//        speedStatus = new MutableLiveData<>(null);

        // retrieve saved instance state
        if (savedStateHandle.contains(SAVED_KEY_SERVICE_STARTED)){
            serviceStatus.setValue(savedStateHandle.get(SAVED_KEY_SERVICE_STARTED));
        }
//        if (savedStateHandle.contains(SAVED_KEY_SPEED_STATUS)){
//            speedStatus.setValue(savedStateHandle.get(SAVED_KEY_SPEED_STATUS));
//        }
        if (savedStateHandle.contains(SAVED_KEY_STOP_MOVING)){
            stopMoving.setValue(savedStateHandle.get(SAVED_KEY_STOP_MOVING));
        }

        // repo stuff
        repository = new RTRepository(application);
        allTracks = repository.getAllTracks();
        lastTrack = repository.getLastTrack();
        allGroupByDateTracks = repository.getAllGroupByDateTracks();

    }

    public void toggleServiceStatus(){
        boolean value = serviceStatus.getValue() != null ? serviceStatus.getValue() : false;
        setValueServiceStatus(!value);
    }

    //--------------------------------------------------------------------------------------------//
    //endregion

    //region VM and Repo Interaction
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

    //--

    public MutableLiveData<Boolean> getStopMoving(){
        return stopMoving;
    }

    public void toggleStopMoving(){
        boolean value = !stopMoving.getValue();
        stopMoving.setValue(value);
        savedState.set(SAVED_KEY_STOP_MOVING, value);
    }

    public void setValueStopMoving(boolean value){
        stopMoving.setValue(value);
        savedState.set(SAVED_KEY_STOP_MOVING, value);
    }

    public boolean getValueStopMoving(){
        return stopMoving.getValue();
    }

    public boolean isJustStarted() {
        return justStarted;
    }

    public void setJustStarted(boolean justStarted) {
        this.justStarted = justStarted;
    }

    //--

//    public MutableLiveData<SpeedStatus> getSpeedStatus() {
//        return speedStatus;
//    }

//    public void setValueSpeedStatus(SpeedStatus value) {
//        speedStatus.setValue(value);
//        savedState.set(SAVED_KEY_SPEED_STATUS, value);
//    }

    //--------------------------------------------------------------------------------------------//
    //endregion

}

