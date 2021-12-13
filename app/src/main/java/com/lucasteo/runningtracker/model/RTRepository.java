package com.lucasteo.runningtracker.model;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class RTRepository {

    private TrackDao trackDao;
    private LiveData<List<Track>> allTracks;

    public RTRepository(Application application) {
        RTRoomDatabase db = RTRoomDatabase.getDatabase(application);

        trackDao = db.trackDao();
        allTracks = trackDao.getTracks();

    }

    public LiveData<List<Track>> getAllTracks() {
        return allTracks;
    }

    public void insert(Track track){
        RTRoomDatabase.databaseWriteExecutor.execute(() -> {
            trackDao.insert(track);
        });
    }
}
