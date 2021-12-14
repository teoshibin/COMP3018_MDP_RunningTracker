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
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.lucasteo.runningtracker.MainActivity;
import com.lucasteo.runningtracker.model.RTRepository;
import com.lucasteo.runningtracker.model.Track;
import com.lucasteo.runningtracker.service.ICallback;
import com.lucasteo.runningtracker.service.TrackerService;

import java.util.List;

/**
 * main view model for storing temporary state
 */
public class MainViewModel extends AndroidViewModel {

    private RTRepository repository;

    private final LiveData<List<Track>> allTracks;
    private TrackerService.TrackerServiceBinder trackerServiceBinder;

    public MainViewModel(@NonNull Application application) {
        super(application);

        // repo stuff
        repository = new RTRepository(application);
        allTracks = repository.getAllTracks();

        Log.d("trackerService", "MainViewModel: ");
        // service stuff

        application.startService(
                new Intent(application, TrackerService.class));
        application.bindService(
                new Intent(application, TrackerService.class),
                serviceConnection, Context.BIND_AUTO_CREATE);

//        this.startService(new Intent(this, PlayerService.class));
//        this.bindService(new Intent(this, PlayerService.class), serviceConnection, Context.BIND_AUTO_CREATE);
    }


    //region Tracker Service
    ICallback callback = new ICallback() {
        @Override
        public void TrackerServiceLocationChange(Location location) {

        }

        @Override
        public void TrackerServiceStatusChange(String provider, int status, Bundle extras) {

        }

        @Override
        public void TrackerServiceOnProviderEnabled(String provider) {

        }

        @Override
        public void TrackerServiceOnProviderDisabled(String provider) {

        }

        //        @Override
        //        public void playerProgressEvent(int progress) {
        //            runOnUiThread(new Runnable() {
        //                @Override
        //                public void run() {
        //                    displayProgress(myBinder.getProgress());
        //                }
        //            });
        //        }
    };

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.d("mainViewModel", "onServiceConnected: MainViewModel");
            trackerServiceBinder = (TrackerService.TrackerServiceBinder) binder;
            trackerServiceBinder.registerCallback(callback);
            // TODO do something on service connect
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("mainViewModel", "onServiceDisconnected: MainViewModel");
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

