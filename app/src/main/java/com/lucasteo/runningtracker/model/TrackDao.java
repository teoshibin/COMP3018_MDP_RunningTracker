package com.lucasteo.runningtracker.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Track track);

    @Query("DELETE FROM track_table")
    void deleteAll();

    @Query("SELECT * FROM track_table ORDER BY trackID ASC")
    LiveData<List<Track>> getTracks();

}

