package com.lucasteo.runningtracker.content_provider;

import android.net.Uri;

/**
 * important singleton for content provider
 * duplicated by external application to interact with our content provider
 */
public class RTContract {

    // authority
    public static final String AUTHORITY = "com.lucasteo.runningtracker.content_provider.RTContentProvider";

    // concrete table attributes
    public static final String TABLE_TRACK = "track_table";
    public static final String COLUMN_TRACK_ID = "trackID";
    public static final String COLUMN_TRACK_LATITUDE = "latitude";
    public static final String COLUMN_TRACK_LONGITUDE = "longitude";
    public static final String COLUMN_TRACK_DISTANCE = "distance";
    public static final String COLUMN_TRACK_ALTITUDE = "altitude";
    public static final String COLUMN_TRACK_SPEED = "speed";
    public static final String COLUMN_TRACK_ACTIVITY = "activity";
    public static final String COLUMN_TRACK_CREATED_TIME = "createdTime";

    // queried abstract table attributes
    public static final String TABLE_GROUP_BY_DATE_TRACK = "group_by_date_track_table";
    public static final String COLUMN_GBDT_NUMBER_OF_RECORDS = "number_of_records";
    public static final String COLUMN_GBDT_TOTAL_DISTANCE = "total_distance";
    public static final String COLUMN_GBDT_AVERAGE_SPEED = "average_speed";
    public static final String COLUMN_GBDT_MAXIMUM_SPEED = "maximum_speed";
    public static final String COLUMN_GBDT_RECORD_DATE = "record_date";

    // table uri
    public static final Uri URI_TRACK = Uri.parse("content://" + AUTHORITY + "/" + TABLE_TRACK);
    public static final Uri URI_GROUP_BY_DATE_TRACK = Uri.parse("content://" + AUTHORITY + "/" + TABLE_GROUP_BY_DATE_TRACK);

    // query type
    public static final String CONTENT_TYPE_SINGLE_TRACK = "vnd.android.cursor.item/" + RTContract.AUTHORITY + "." + RTContract.TABLE_TRACK;
    public static final String CONTENT_TYPE_MULTIPLE_TRACKS = "vnd.android.cursor.dir/" + RTContract.AUTHORITY + "." + RTContract.TABLE_TRACK;
    public static final String CONTENT_TYPE_SINGLE_GROUP_BY_DATE_TRACK = "vnd.android.cursor.item/" + RTContract.AUTHORITY + "." + RTContract.TABLE_GROUP_BY_DATE_TRACK;
    public static final String CONTENT_TYPE_MULTIPLE_GROUP_BY_DATE_TRACKS = "vnd.android.cursor.dir/" + RTContract.AUTHORITY + "." + RTContract.TABLE_GROUP_BY_DATE_TRACK;

    // extra data type
    public static final String ACTIVITY_STANDING = "STANDING";
    public static final String ACTIVITY_WALKING = "WALKING";
    public static final String ACTIVITY_JOGGING = "JOGGING";
    public static final String ACTIVITY_RUNNING = "RUNNING";
    public static final String ACTIVITY_CYCLING = "CYCLING";
    public static final String ACTIVITY_DRIVING = "DRIVING";
}
