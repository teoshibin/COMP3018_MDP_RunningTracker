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

public class RTContentProvider extends ContentProvider {
    // TODO declare provider in manifest

    private static final UriMatcher MATCHER;

    private static final int CODE_TRACK_DIR = 0;
    private static final int CODE_TRACK_ITEM = 1;
    private static final int CODE_GROUP_BY_DATE_TRACK_DIR = 10;
    private static final int CODE_GROUP_BY_DATE_TRACK_ITEM = 11;

    static {
        MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        MATCHER.addURI(RTContract.AUTHORITY, RTContract.TRACK_TABLE, CODE_TRACK_DIR); // entire table
        MATCHER.addURI(RTContract.AUTHORITY, RTContract.TRACK_TABLE + "/#", CODE_TRACK_ITEM); // specific id
        MATCHER.addURI(RTContract.AUTHORITY, RTContract.TABLE_GROUP_BY_DATE_TRACK, CODE_GROUP_BY_DATE_TRACK_DIR); // entire table
        MATCHER.addURI(RTContract.AUTHORITY, RTContract.TABLE_GROUP_BY_DATE_TRACK + "/#", CODE_GROUP_BY_DATE_TRACK_ITEM); // specific id
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final Context context = getContext();

        if (context == null) {
            return null;
        }

        TrackDao trackDao = RTRoomDatabase.getDatabase(context).trackDao();
        final Cursor cursor;

        // parse uri and do different types of queries based on code
        switch (MATCHER.match(uri)){
            case CODE_TRACK_DIR:
                return trackDao.getCursorTracks();
            case CODE_TRACK_ITEM:
                return trackDao.getCursorTrackById(ContentUris.parseId(uri));
            case CODE_GROUP_BY_DATE_TRACK_ITEM:
                return null;
            case CODE_GROUP_BY_DATE_TRACK_DIR:
                return null;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
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
        String contentType;
        if (uri.getLastPathSegment() == null) {
            contentType = RTContract.CONTENT_TYPE_MULTIPLE;
        } else {
            contentType = RTContract.CONTENT_TYPE_SINGLE;
        }
        return contentType;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
