package com.lucasteo.runningtracker.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "track_table")
public class Track {

    @PrimaryKey(autoGenerate = true)
    private int trackID;
    @ColumnInfo(defaultValue = "0")
    private int distance;
//    private int calculatedSteps;
//    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
//    private String createdTime;
//    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
//    private String lastModifiedTime;

    public Track(int trackID, int distance) {
        this.trackID = trackID;
        this.distance = distance;
    }

    public int getTrackID() {
        return trackID;
    }
    public int getDistance() {
        return distance;
    }
//    public String getCreatedTime() {
//        return createdTime;
//    }
//    public String getLastModifiedTime() {
//        return lastModifiedTime;
//    }
}
