package com.lucasteo.runningtracker.content_provider;

import android.net.Uri;

public class RTContract {

    // authority
    public static final String AUTHORITY = "com.lucasteo.runningtracker.content_provider.RTContentProvider";

    // concrete table attributes
    public static final String TRACK_TABLE = "track_table";
    public static final String COLUMN_TRACK_ID = "trackID";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_DISTANCE = "distance";
    public static final String COLUMN_ALTITUDE = "altitude";
    public static final String COLUMN_SPEED = "speed";
    public static final String COLUMN_ACTIVITY = "activity";
    public static final String COLUMN_CREATED_TIME = "createdTime";

    // queried abstract table attributes
    public static final String GROUP_BY_DATE_TRACK_TABLE = "group_by_date_track_table";
    public static final String COLUMN_NUMBER_OF_RECORDS = "number_of_records";
    public static final String COLUMN_TOTAL_DISTANCE = "total_distance";
    public static final String COLUMN_AVERAGE_SPEED = "average_speed";
    public static final String COLUMN_MAXIMUM_SPEED = "maximum_speed";
    public static final String COLUMN_RECORD_DATE = "record_date";

    // table uri
    public static final Uri URI_TRACK = Uri.parse("content://" + AUTHORITY + "/" + TRACK_TABLE);
    public static final Uri URI_GROUP_BY_DATE_TRACK = Uri.parse("content://" + AUTHORITY + "/" + GROUP_BY_DATE_TRACK_TABLE);

    // query type
    public static final String CONTENT_TYPE_SINGLE_TRACK = "vnd.android.cursor.item/" + RTContract.AUTHORITY + "." + RTContract.TRACK_TABLE;
    public static final String CONTENT_TYPE_MULTIPLE_TRACKS = "vnd.android.cursor.dir/" + RTContract.AUTHORITY + "." + RTContract.TRACK_TABLE;
    public static final String CONTENT_TYPE_SINGLE_GROUP_BY_DATE_TRACK = "vnd.android.cursor.item/" + RTContract.AUTHORITY + "." + RTContract.GROUP_BY_DATE_TRACK_TABLE;
    public static final String CONTENT_TYPE_MULTIPLE_GROUP_BY_DATE_TRACKS = "vnd.android.cursor.dir/" + RTContract.AUTHORITY + "." + RTContract.GROUP_BY_DATE_TRACK_TABLE;

    // extra data type
    public static final String ACTIVITY_STANDING = "STANDING";
    public static final String ACTIVITY_WALKING = "WALKING";
    public static final String ACTIVITY_JOGGING = "JOGGING";
    public static final String ACTIVITY_RUNNING = "RUNNING";
    public static final String ACTIVITY_CYCLING = "CYCLING";
    public static final String ACTIVITY_DRIVING = "DRIVING";
}
