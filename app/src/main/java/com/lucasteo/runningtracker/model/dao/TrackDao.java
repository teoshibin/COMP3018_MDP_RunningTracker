package com.lucasteo.runningtracker.model.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.lucasteo.runningtracker.model.pojo.GroupByDateTrackPojo;
import com.lucasteo.runningtracker.model.entity.Track;

import java.util.List;

@Dao
public interface TrackDao {

    // standard

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Track track);

    @Query("DELETE FROM track_table")
    void deleteAll();

    @Query("SELECT * FROM track_table ORDER BY trackID ASC")
    LiveData<List<Track>> getTracks();

    @Query("SELECT * FROM track_table ORDER BY trackID DESC LIMIT 1")
    LiveData<Track> getLastTrack();

    @Query("SELECT " +
            "count(trackID) AS number_of_records, " +
            "round(sum(distance),2) AS total_distance, " +
            "round(avg(speed),2) AS average_speed, " +
            "round(max(speed),2) AS maximum_speed, " +
            "date(CAST(createdTime/1000 AS INTEGER), 'unixepoch') AS record_date " +
            "FROM track_table " +
            "GROUP BY record_date " +
            "ORDER BY record_date DESC")
    LiveData<List<GroupByDateTrackPojo>> getGroupByDateTracks();

    // cursor for content provider

    @Query("SELECT * FROM track_table ORDER BY trackID ASC")
    Cursor getCursorTracks();

    @Query("SELECT * FROM track_table WHERE trackID = :id ORDER BY trackID ASC")
    Cursor getCursorTrackById(long id);

}

