package com.lucasteo.runningtracker.model.entity;

import android.content.ContentValues;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.lucasteo.runningtracker.calculation.SpeedStatus;

import java.util.Date;

@Entity(tableName = Track.TABLE_NAME)
public class Track {

    public static final String TABLE_NAME = "track_table";
    public static final String COLUMN_TRACK_ID = "trackID";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_DISTANCE = "distance";
    public static final String COLUMN_ALTITUDE = "altitude";
    public static final String COLUMN_SPEED = "speed";
    public static final String COLUMN_ACTIVITY = "activity";
    public static final String COLUMN_CREATED_TIME = "createdTime";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_TRACK_ID)
    private long trackID;
    @ColumnInfo(name = COLUMN_LATITUDE)
    private double latitude;
    @ColumnInfo(name = COLUMN_LONGITUDE)
    private double longitude;
    @ColumnInfo(name = COLUMN_DISTANCE)
    private double distance;
    @ColumnInfo(name = COLUMN_ALTITUDE)
    private double altitude;
    @ColumnInfo(name = COLUMN_SPEED)
    private double speed;
    @ColumnInfo(name = COLUMN_ACTIVITY)
    private String activity;
    @ColumnInfo(name = COLUMN_CREATED_TIME)
    private Date createdTime;

    public Track(long trackID, double latitude, double longitude,
                 double distance, double altitude, double speed,
                 String activity, Date createdTime) {
        this.trackID = trackID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
        this.altitude = altitude;
        this.speed = speed;
        this.activity = activity;
        this.createdTime = createdTime;
    }

    public long getTrackID() {
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

    @NonNull
    public static Track fromContentValues(@Nullable ContentValues values){

        long trackID = 0;
        double latitude = 0;
        double longitude = 0;
        double distance = 0;
        double altitude = 0;
        double speed = 0;
        String activity = SpeedStatus.STANDING.name();
        Date createdTime = new Date();

        if (values != null && values.containsKey(COLUMN_TRACK_ID)){
            trackID = values.getAsLong(COLUMN_TRACK_ID);
        }
        if (values != null && values.containsKey(COLUMN_LATITUDE)){
            latitude = values.getAsDouble(COLUMN_LATITUDE);
        }
        if (values != null && values.containsKey(COLUMN_LONGITUDE)){
            longitude = values.getAsDouble(COLUMN_LONGITUDE);
        }
        if (values != null && values.containsKey(COLUMN_DISTANCE)){
            distance = values.getAsDouble(COLUMN_DISTANCE);
        }
        if (values != null && values.containsKey(COLUMN_ALTITUDE)){
            altitude = values.getAsDouble(COLUMN_ALTITUDE);
        }
        if (values != null && values.containsKey(COLUMN_SPEED)){
            speed = values.getAsDouble(COLUMN_SPEED);
        }
        if (values != null && values.containsKey(COLUMN_ACTIVITY)){
            activity = values.getAsString(COLUMN_ACTIVITY);
        }
//        if (values != null && values.containsKey(COLUMN_CREATED_TIME)){
//            createdTime = (Date) values.get(COLUMN_CREATED_TIME);
//        }

        return new Track(trackID, latitude, longitude, distance, altitude, speed, activity, createdTime);
    }
}
