package com.lucasteo.runningtracker.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "track_table")
public class Track {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "trackID")
    private int trackID;

    private int distance;

    public Track(@NonNull int trackID, int distance) {
        this.trackID = trackID;
        this.distance = distance;
    }

    public int getTrackID() {
        return trackID;
    }

    public int getDistance() {
        return distance;
    }
}
