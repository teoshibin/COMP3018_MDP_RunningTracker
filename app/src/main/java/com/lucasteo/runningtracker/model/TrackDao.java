package com.lucasteo.runningtracker.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

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

    @Query("SELECT " +
            "count(trackID) AS 'numberOfRecords', " +
            "round(sum(distance),2) AS 'totalDistance', " +
            "round(avg(speed),2) AS 'averageSpeed', " +
            "round(max(speed),2) AS 'maximumSpeed', " +
            "date(CAST(createdTime/1000 AS INTEGER), 'unixepoch') AS 'date' " +
            "FROM track_table " +
            "GROUP BY date(CAST(createdTime/1000 AS INTEGER), 'unixepoch')")
    LiveData<List<GroupByDateTrack>> getGroupByDateTracks();

//    // PAST X PERIOD
//
//    @Query("SELECT * FROM track_table " +
//            "WHERE createdTime > (strftime('%s', 'now', '-1 day') || substr(strftime('%f','now'),4)) " +
//            "ORDER BY trackID ASC")
//    LiveData<List<Track>> getTracksPastOneDay();
//
//    @Query("SELECT * FROM track_table " +
//            "WHERE createdTime > (strftime('%s', 'now', '-7 day') || substr(strftime('%f','now'),4)) " +
//            "ORDER BY trackID ASC")
//    LiveData<List<Track>> getTracksPastOneWeek();
//
//    @Query("SELECT * FROM track_table " +
//            "WHERE createdTime > (strftime('%s', 'now', '-1 month') || substr(strftime('%f','now'),4)) " +
//            "ORDER BY trackID ASC")
//    LiveData<List<Track>> getTracksPastOneMonth();
//
//    @Query("SELECT * FROM track_table " +
//            "WHERE createdTime > (strftime('%s', 'now', '-1 year') || substr(strftime('%f','now'),4)) " +
//            "ORDER BY trackID ASC")
//    LiveData<List<Track>> getTracksPastOneYear();
//
//    // THIS X PERIOD
//
//    @Query("SELECT * FROM track_table " +
//            "WHERE createdTime BETWEEN " +
//            "strftime('%s000',datetime('now','localtime','start of day')) " +
//            "AND " +
//            "strftime('%s999', datetime('now','localtime','start of day', '+1 day', '-0.001 second')) " +
//            "ORDER BY trackID ASC")
//    LiveData<List<Track>> getTracksToday();
//
//    @Query("SELECT * FROM track_table " +
//            "WHERE createdTime BETWEEN " +
//            "strftime('%s000', datetime('now','localtime','start of day','-1 day')) " +
//            "AND " +
//            "strftime('%s999', datetime('now','localtime','start of day','-0.001 second')) " +
//            "ORDER BY trackID ASC")
//    LiveData<List<Track>> getTracksYesterday();
//
//    @Query("SELECT * FROM track_table " +
//            "WHERE createdTime BETWEEN " +
//            "strftime('%s000',datetime('now','localtime','start of day','weekday 6','-6 days')) " +
//            "AND " +
//            "strftime('%s999',datetime('now','localtime','start of day','+1 day','-0.001 second','weekday 6')) " +
//            "ORDER BY trackID ASC")
//    LiveData<List<Track>> getTracksThisWeekSUN();
//
//    @Query("SELECT * FROM track_table " +
//            "WHERE createdTime BETWEEN " +
//            "strftime('%s000', datetime('now','localtime','start of day','weekday 0','-6 days')) " +
//            "AND " +
//            "strftime('%s999', datetime('now','localtime','start of day','+1 day','-0.001 second','weekday 0')) " +
//            "ORDER BY trackID ASC")
//    LiveData<List<Track>> getTracksThisWeekMON();
//
//    @Query("SELECT * FROM track_table " +
//            "WHERE createdTime BETWEEN " +
//            "strftime('%s000', datetime('now','localtime','start of month')) " +
//            "AND " +
//            "strftime('%s999', datetime('now','localtime','start of month','+1 month','-0.001 second')) " +
//            "ORDER BY trackID ASC")
//    LiveData<List<Track>> getTracksThisMonth();
//
//    @Query("SELECT * FROM track_table " +
//            "WHERE createdTime BETWEEN " +
//            "strftime('%s000', datetime('now','localtime','start of year')) " +
//            "AND " +
//            "strftime('%s999', datetime('now','localtime','start of year','+1 year','-0.001 second')) " +
//            "ORDER BY trackID ASC")
//    LiveData<List<Track>> getTracksThisYear();


}

