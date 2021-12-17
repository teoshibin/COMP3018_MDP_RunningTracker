package com.lucasteo.runningtracker.viewmodel;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lucasteo.runningtracker.model.RTRepository;
import com.lucasteo.runningtracker.model.Track;
import com.lucasteo.runningtracker.service.ICallback;
import com.lucasteo.runningtracker.service.TrackerService;

import java.util.List;

/**
 * main view model for storing temporary state
 */
public class MainViewModel extends AndroidViewModel {

    // debug log
    private static final String TAG = "MainViewModel";

    // repo
    private RTRepository repository;
    private final LiveData<List<Track>> allTracks;

    // service
    private TrackerService.TrackerServiceBinder trackerServiceBinder;

    /**
     * Main Activity ViewModel
     *
     * @param application application reference for retrieving repository
     */
    public MainViewModel(@NonNull Application application) {
        super(application);
        Log.d(TAG, "MainViewModel: Instantiated");

        // repo stuff
        repository = new RTRepository(application);
        allTracks = repository.getAllTracks();

    }

    //region SERVICE
    public void startTrackerService(){
        // normal code to start service in activity
        // this.startForegroundService(new Intent(this, TrackerService.class));
        // this.bindService(new Intent(this, TrackerService.class),
        //         serviceConnection, Context.BIND_AUTO_CREATE);

        // start service in view model
        Application applicationRef = getApplication();
        applicationRef.startForegroundService(
                new Intent(applicationRef, TrackerService.class));
        applicationRef.bindService(
                new Intent(applicationRef, TrackerService.class),
                serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public void StopTrackerService(){
        trackerServiceBinder.stopTrackerService();
    }

    ICallback callback = new ICallback() {
        // to use this remember to use runOnUiThread new Runnable()
    };

    private final ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.d(TAG, "onServiceConnected: MainActivity");
            trackerServiceBinder = (TrackerService.TrackerServiceBinder) binder;
            trackerServiceBinder.registerCallback(callback);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected: MainActivity");
            trackerServiceBinder.unregisterCallback(callback);
            trackerServiceBinder = null;
        }
    };
    //endregion

    //region VM and Repo Interaction
    public void insert(Track track) {
        repository.insert(track);
    }

    public LiveData<List<Track>> getAllTracks() {
        return allTracks;
    }
    //endregion

//    // save state keys
//    private final String SAVED_KEY_SHAPE = "shape";
//    private final String SAVED_KEY_SIZE = "size";
//    private final String SAVED_KEY_COLOR = "color";
//
//    // variables
//    SavedStateHandle savedState;
//    public MutableLiveData<Integer> brushColor = new MutableLiveData<>(Integer.valueOf(Color.BLACK));
//    public MutableLiveData<Paint.Cap> brushShape = new MutableLiveData<>(Paint.Cap.ROUND);
//    public MutableLiveData<Integer> brushSize = new MutableLiveData<>(20);
//
//    // constructor for recovering state
//    public MainViewModel(SavedStateHandle savedStateHandle){
//        this.savedState = savedStateHandle;
//        if(savedStateHandle.contains(SAVED_KEY_COLOR)){
//            brushColor.setValue(savedStateHandle.get(SAVED_KEY_COLOR));
//        }
//        if(savedStateHandle.contains(SAVED_KEY_SHAPE)){
//            brushShape.setValue(savedStateHandle.get(SAVED_KEY_SHAPE));
//        }
//        if(savedStateHandle.contains(SAVED_KEY_SIZE)){
//            brushSize.setValue(savedStateHandle.get(SAVED_KEY_SIZE));
//        }
//    }
//
//    public int getBrushSize() {
//        return brushSize.getValue();
//    }
//
//    public Paint.Cap getBrushShape() {
//        return brushShape.getValue();
//    }
//
//    public int getBrushColor() {
//        return brushColor.getValue();
//    }
//
//    public void setBrushSize(int brushSize) {
//        this.brushSize.setValue(brushSize);
//        savedState.set(SAVED_KEY_SIZE, brushSize);
//    }
//
//    public void setBrushShape(Paint.Cap brushShape) {
//        this.brushShape.setValue(brushShape);
//        savedState.set(SAVED_KEY_SHAPE, brushShape);
//    }
//
//    public void setBrushColor(int brushColor) {
//        this.brushColor.setValue(brushColor);
//        savedState.set(SAVED_KEY_COLOR, brushColor);
//    }

}

