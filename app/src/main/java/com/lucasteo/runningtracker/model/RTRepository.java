package com.lucasteo.runningtracker.model;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;

public class RTRepository {

    // DAO
    private TrackDao trackDao;

    // Data
    private LiveData<List<Track>> allTracks;
    private LiveData<List<Track>> tracksOnDate;

    /**
     * repository constructor
     * initiate all data from db into repo using application as reference
     * @param application reference to database
     */
    public RTRepository(Application application) {
        RTRoomDatabase db = RTRoomDatabase.getDatabase(application);

        // retrieving tracks
        trackDao = db.trackDao();
        allTracks = trackDao.getTracks();
        tracksOnDate = allTracks;

    }

    public LiveData<List<Track>> getAllTracks() {
        return allTracks;
    }

    // CRUD

    public void insert(Track track){
        RTRoomDatabase.databaseWriteExecutor.execute(() -> {
            trackDao.insert(track);
        });
    }

}
