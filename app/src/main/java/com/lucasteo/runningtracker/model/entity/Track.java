package com.lucasteo.runningtracker.model.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "track_table")
public class Track {

    @PrimaryKey(autoGenerate = true)
    private final int trackID;
    private final double latitude;
    private final double longitude;
    private final double distance;
    private final double altitude;
    private final double speed;
    private final String activity;
    private final Date createdTime;

    public Track(int trackID, double latitude, double longitude,
                 double distance, double altitude, double speed, String activity, Date createdTime) {
        this.trackID = trackID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
        this.altitude = altitude;
        this.speed = speed;
        this.activity = activity;
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
    public double getLongitude() {
        return longitude;
    }
    public double getAltitude() {
        return altitude;
    }
    public double getSpeed() {
        return speed;
    }
    public String getActivity() {
        return activity;
    }
    public Date getCreatedTime() {
        return createdTime;
    }
}
