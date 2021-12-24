package com.lucasteo.runningtracker.model;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.lucasteo.runningtracker.model.dao.TrackDao;
import com.lucasteo.runningtracker.model.entity.Track;
import com.lucasteo.runningtracker.model.pojo.GroupByDateTrackPojo;

import java.util.List;

/**
 * repository
 */
public class RTRepository {

    // DAO
    private TrackDao trackDao;

    // Data
    private LiveData<List<Track>> allTracks;
    private LiveData<Track> lastTrack;
    private LiveData<List<GroupByDateTrackPojo>> allGroupByDateTracks;

    /**
     * repository constructor
     * initiate all data from db into repo using application as reference
     * @param application reference to database
     */
    public RTRepository(Application application) {
        RTRoomDatabase db = RTRoomDatabase.getDatabase(application);

        // retrieving tracks dao
        trackDao = db.trackDao();

        // initialize data
        allTracks = trackDao.getTracks();
        lastTrack = trackDao.getLastTrack();
        allGroupByDateTracks = trackDao.getGroupByDateTracks();

    }

    // standard queries

    public void insert(Track track){
        RTRoomDatabase.databaseWriteExecutor.execute(() -> {
            trackDao.insert(track);
        });
    }

    public void deleteAll(){
        RTRoomDatabase.databaseWriteExecutor.execute(() -> {
            trackDao.deleteAll();
        });
    }

    // setter getter

    public LiveData<List<Track>> getAllTracks() {
        return allTracks;
    }

    public LiveData<Track> getLastTrack(){
        return lastTrack;
    }

    public LiveData<List<GroupByDateTrackPojo>> getAllGroupByDateTracks(){
        return allGroupByDateTracks;
    }

}
