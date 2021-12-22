package com.lucasteo.runningtracker.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateHandle;

import com.lucasteo.runningtracker.model.GroupByDateTrackPojo;
import com.lucasteo.runningtracker.model.RTRepository;
import com.lucasteo.runningtracker.model.Track;
import com.lucasteo.runningtracker.service.SpeedStatus;

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

    // repo
    private RTRepository repository;
    private final LiveData<List<Track>> allTracks;
    private final LiveData<List<GroupByDateTrackPojo>> allGroupByDateTracks;

    // UI states
    private MutableLiveData<Boolean> serviceStatus;
    private MutableLiveData<SpeedStatus> speedStatus;

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
        speedStatus = new MutableLiveData<>(null);

        // retrieve saved instance state
        if (savedStateHandle.contains(SAVED_KEY_SERVICE_STARTED)){
            serviceStatus.setValue(savedStateHandle.get(SAVED_KEY_SERVICE_STARTED));
        }
        if (savedStateHandle.contains(SAVED_KEY_SPEED_STATUS)){
            speedStatus.setValue(savedStateHandle.get(SAVED_KEY_SPEED_STATUS));
        }

        // repo stuff
        repository = new RTRepository(application);
        allTracks = repository.getAllTracks();
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

    public MutableLiveData<SpeedStatus> getSpeedStatus() {
        return speedStatus;
    }

    public void setValueSpeedStatus(SpeedStatus value) {
        speedStatus.setValue(value);
        savedState.set(SAVED_KEY_SPEED_STATUS, value);
    }

    public SpeedStatus getValueSpeedStatus(){
        return speedStatus.getValue();
    }

    //--------------------------------------------------------------------------------------------//
    //endregion

}

