package com.lucasteo.runningtracker.content_provider;

import android.net.Uri;

public class RTContract {

    // authority
    public static final String AUTHORITY = "com.lucasteo.runningtracker.content_provider.RTContentProvider";

    // concrete table attributes
    public static final String TRACK_TABLE = "track_table";
    public static final String TRACK_PRIMARY_KEY = "trackID";
    public static final String LATITUDE_COLUMN = "latitude";
    public static final String LONGITUDE_COLUMN = "longitude";
    public static final String DISTANCE_COLUMN = "distance";
    public static final String ALTITUDE_COLUMN = "altitude";
    public static final String SPEED_COLUMN = "speed";
    public static final String ACTIVITY_COLUMN = "activity";
    public static final String CREATED_TIME_COLUMN = "createdTime";

    // queried abstract table attributes
    public static final String TABLE_GROUP_BY_DATE_TRACK = "group_by_date_track_table";
    public static final String NUMBER_OF_RECORDS_COLUMN = "number_of_records";
    public static final String TOTAL_DISTANCE_COLUMN = "total_distance";
    public static final String AVERAGE_SPEED_COLUMN = "average_speed";
    public static final String MAXIMUM_SPEED_COLUMN = "maximum_speed";
    public static final String RECORD_DATE_COLUMN = "record_date";

    // table uri
    public static final Uri URI_TRACK = Uri.parse("content://" + AUTHORITY + "/" + TRACK_TABLE);
    public static final Uri URI_GROUP_BY_DATE_TRACK = Uri.parse("content://" + AUTHORITY + "/" + TABLE_GROUP_BY_DATE_TRACK);

    // query type
    public static final String CONTENT_TYPE_SINGLE = "vnd.android.cursor.item/RTContentProvider.data.text";
    public static final String CONTENT_TYPE_MULTIPLE = "vnd.android.cursor.dir/RTContentProvider.data.text";

}
