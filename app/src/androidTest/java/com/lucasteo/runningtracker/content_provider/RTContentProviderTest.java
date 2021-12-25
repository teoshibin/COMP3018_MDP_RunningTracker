package com.lucasteo.runningtracker.content_provider;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import com.lucasteo.runningtracker.calculation.SpeedStatus;
import com.lucasteo.runningtracker.model.RTRoomDatabase;
import com.lucasteo.runningtracker.model.entity.Track;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class RTContentProviderTest {

    private ContentResolver mContentResolver;

    @Before
    public void setUp() throws Exception {
        final Context context = ApplicationProvider.getApplicationContext();
        RTRoomDatabase.switchToInMemory(context);
        mContentResolver = context.getContentResolver();
    }

    @Test
    public void track_initiallyEmpty() {
        // database should be empty using query method from content provider
        final Cursor cursor = mContentResolver.query(RTContract.URI_TRACK,
                new String[]{Track.COLUMN_DISTANCE}, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(0));
        cursor.close();
    }

    @Test
    public void insert() {

        final Uri itemUri = mContentResolver.insert(RTContract.URI_TRACK,
                createContentValues(-1.1,-2.2,3.3,-4.4,5.5, RTContract.ACTIVITY_RUNNING));

        assertThat(itemUri, notNullValue());

        final Cursor cursor = mContentResolver.query(RTContract.URI_TRACK,
                null, null, null, null);

        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(1));
        assertThat(cursor.moveToFirst(), is(true));
        assertThat(cursor.getDouble(cursor.getColumnIndexOrThrow(RTContract.COLUMN_LATITUDE)), is(-1.1));
        assertThat(cursor.getDouble(cursor.getColumnIndexOrThrow(RTContract.COLUMN_LONGITUDE)), is(-2.2));
        assertThat(cursor.getDouble(cursor.getColumnIndexOrThrow(RTContract.COLUMN_DISTANCE)), is(3.3));
        assertThat(cursor.getDouble(cursor.getColumnIndexOrThrow(RTContract.COLUMN_ALTITUDE)), is(-4.4));
        assertThat(cursor.getDouble(cursor.getColumnIndexOrThrow(RTContract.COLUMN_SPEED)), is(5.5));
        assertThat(cursor.getString(cursor.getColumnIndexOrThrow(RTContract.COLUMN_ACTIVITY)), is(RTContract.ACTIVITY_RUNNING));

        cursor.close();
    }

    private ContentValues createContentValues(
            double latitude, double longitude, double distance,
            double altitude, double speed, String activity)
    {
        final ContentValues values = new ContentValues();
        values.put(RTContract.COLUMN_LATITUDE, latitude);
        values.put(RTContract.COLUMN_LONGITUDE, longitude);
        values.put(RTContract.COLUMN_DISTANCE, distance);
        values.put(RTContract.COLUMN_ALTITUDE, altitude);
        values.put(RTContract.COLUMN_SPEED, speed);
        values.put(RTContract.COLUMN_ACTIVITY, activity);
        return values;
    }
}