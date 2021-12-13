package com.lucasteo.runningtracker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.lucasteo.runningtracker.model.RTRepository;
import com.lucasteo.runningtracker.model.Track;

import java.util.List;

/**
 * main view model for storing temporary state
 */
public class MainViewModel extends AndroidViewModel {

    private RTRepository repository;

    private final LiveData<List<Track>> allTracks;

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new RTRepository(application);
        allTracks = repository.getAllTracks();
    }

    public LiveData<List<Track>> getAllTracks() {
        return allTracks;
    }

    public void insert(Track track) {
        repository.insert(track);
    }

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

