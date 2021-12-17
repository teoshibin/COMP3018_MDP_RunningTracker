package com.lucasteo.runningtracker.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "track_table")
public class Track {

    @PrimaryKey(autoGenerate = true)
    private int trackID;
    private double latitude;
    private double longtitude;
    private double distance;
    private double altitude;
    private double speed;
    private Date createdTime;

    public Track(int trackID, double latitude, double longtitude,
                 double distance, double altitude, double speed, Date createdTime) {
        this.trackID = trackID;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.distance = distance;
        this.altitude = altitude;
        this.speed = speed;
        this.createdTime = createdTime;
    }

    public int getTrackID() {
        return trackID;
    }
    public double getDistance() {
        return distance;
    }
    public double getLatitude() {
        return latitude;
    }
    public double getLongtitude() {
        return longtitude;
    }
    public double getAltitude() {
        return altitude;
    }
    public double getSpeed() {
        return speed;
    }
    public Date getCreatedTime() {
        return createdTime;
    }
}
