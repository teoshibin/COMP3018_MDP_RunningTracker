package com.lucasteo.runningtracker.content_provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lucasteo.runningtracker.model.RTRoomDatabase;
import com.lucasteo.runningtracker.model.dao.TrackDao;
import com.lucasteo.runningtracker.model.entity.Track;

/**
 * content provider allowing database access for external application
 */
public class RTContentProvider extends ContentProvider {

    // string uri matcher
    private static final UriMatcher MATCHER;

    // matched code for uri matcher
    private static final int CODE_TRACK_DIR = 0;
    private static final int CODE_TRACK_ITEM = 1;
    private static final int CODE_GROUP_BY_DATE_TRACK_DIR = 10;
    private static final int CODE_GROUP_BY_DATE_TRACK_ITEM = 11;

    // define uri to it's corresponding code
    static {
        MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        MATCHER.addURI(RTContract.AUTHORITY, RTContract.TABLE_TRACK, CODE_TRACK_DIR); // entire table
        MATCHER.addURI(RTContract.AUTHORITY, RTContract.TABLE_TRACK + "/#", CODE_TRACK_ITEM); // specific id
        MATCHER.addURI(RTContract.AUTHORITY, RTContract.TABLE_GROUP_BY_DATE_TRACK, CODE_GROUP_BY_DATE_TRACK_DIR); // entire table
        MATCHER.addURI(RTContract.AUTHORITY, RTContract.TABLE_GROUP_BY_DATE_TRACK + "/#", CODE_GROUP_BY_DATE_TRACK_ITEM); // specific id
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    /**
     * query method of content provider
     *
     * @param uri uri
     * @param projection projection containing column names
     * @param selection selection string
     * @param selectionArgs selection arguments
     * @param sortOrder sort order
     * @return cursor pointing our database
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // TODO deal with each parameter

        final Context context = getContext();

        if (context == null) {
            return null;
        }

        TrackDao trackDao = RTRoomDatabase.getDatabase(context).trackDao();
        final Cursor cursor;

        // parse uri and do different types of queries based on code
        switch (MATCHER.match(uri)){
            case CODE_TRACK_DIR:
                cursor = trackDao.getCursorTracks();
                break;
            case CODE_TRACK_ITEM:
                cursor = trackDao.getCursorTrackById(ContentUris.parseId(uri));
                break;
            case CODE_GROUP_BY_DATE_TRACK_DIR:
                cursor = trackDao.getCursorGroupByDateTracks();
                break;
            case CODE_GROUP_BY_DATE_TRACK_ITEM:
                cursor = trackDao.getCursorGroupByDateTrackById(ContentUris.parseId(uri));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        cursor.setNotificationUri(context.getContentResolver(), uri);
        return cursor;
    }

    /**
     * get uri query item number type
     *
     * @param uri uri
     * @return content type of this uri query
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (MATCHER.match(uri)) {
            case CODE_TRACK_DIR:
                return RTContract.CONTENT_TYPE_MULTIPLE_TRACKS;
            case CODE_TRACK_ITEM:
                return RTContract.CONTENT_TYPE_SINGLE_TRACK;
            case CODE_GROUP_BY_DATE_TRACK_DIR:
                return RTContract.CONTENT_TYPE_MULTIPLE_GROUP_BY_DATE_TRACKS;
            case CODE_GROUP_BY_DATE_TRACK_ITEM:
                return RTContract.CONTENT_TYPE_SINGLE_GROUP_BY_DATE_TRACK;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        // other application shouldn't be inserting random data into our database but this is written
        // just for demonstration

        final Context context = getContext();
        if (context == null) {
            return null;
        }

        switch (MATCHER.match(uri)) {
            case CODE_TRACK_DIR:

                TrackDao trackDao = RTRoomDatabase.getDatabase(context).trackDao();
                final long id = trackDao.insert(Track.fromContentValues(contentValues));

                context.getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);

            case CODE_GROUP_BY_DATE_TRACK_DIR:
                throw new UnsupportedOperationException(
                        "Invalid URI, \"" + RTContract.TABLE_GROUP_BY_DATE_TRACK +
                        "\" is an virtual table queried from \"" +
                        RTContract.TABLE_TRACK + "\", insert is not possible : " + uri);
            case CODE_TRACK_ITEM:
            case CODE_GROUP_BY_DATE_TRACK_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with URI containing ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        // other application shouldn't be deleting our data
        throw new UnsupportedOperationException("Delete Operation is disabled");
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        // update of track data is pointless, not even our application uses update
        throw new UnsupportedOperationException("Update Operation is disabled");
    }
}
