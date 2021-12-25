package com.lucasteo.runningtracker.content_provider;

import static org.junit.Assert.*;

import com.lucasteo.runningtracker.calculation.SpeedStatus;
import com.lucasteo.runningtracker.model.entity.Track;
import com.lucasteo.runningtracker.model.pojo.GroupByDateTrackPojo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RTContractTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void check_value_all_correct(){

        // track table and column names
        assertEquals(Track.TABLE_NAME, RTContract.TABLE_TRACK);
        assertEquals(Track.COLUMN_TRACK_ID, RTContract.COLUMN_TRACK_ID);
        assertEquals(Track.COLUMN_LATITUDE, RTContract.COLUMN_TRACK_LATITUDE);
        assertEquals(Track.COLUMN_LONGITUDE, RTContract.COLUMN_TRACK_LONGITUDE);
        assertEquals(Track.COLUMN_DISTANCE, RTContract.COLUMN_TRACK_DISTANCE);
        assertEquals(Track.COLUMN_ALTITUDE, RTContract.COLUMN_TRACK_ALTITUDE);
        assertEquals(Track.COLUMN_SPEED, RTContract.COLUMN_TRACK_SPEED);
        assertEquals(Track.COLUMN_ACTIVITY, RTContract.COLUMN_TRACK_ACTIVITY);
        assertEquals(Track.COLUMN_CREATED_TIME, RTContract.COLUMN_TRACK_CREATED_TIME);

        // queried column names
        assertEquals(GroupByDateTrackPojo.COLUMN_NUMBER_OF_RECORDS, RTContract.COLUMN_GBDT_NUMBER_OF_RECORDS);
        assertEquals(GroupByDateTrackPojo.COLUMN_TOTAL_DISTANCE, RTContract.COLUMN_GBDT_TOTAL_DISTANCE);
        assertEquals(GroupByDateTrackPojo.COLUMN_AVERAGE_SPEED, RTContract.COLUMN_GBDT_AVERAGE_SPEED);
        assertEquals(GroupByDateTrackPojo.COLUMN_MAXIMUM_SPEED, RTContract.COLUMN_GBDT_MAXIMUM_SPEED);
        assertEquals(GroupByDateTrackPojo.COLUMN_RECORD_DATE, RTContract.COLUMN_GBDT_RECORD_DATE);

        // speed status
        assertEquals(SpeedStatus.STANDING.name(), RTContract.ACTIVITY_STANDING);
        assertEquals(SpeedStatus.WALKING.name(), RTContract.ACTIVITY_WALKING);
        assertEquals(SpeedStatus.JOGGING.name(), RTContract.ACTIVITY_JOGGING);
        assertEquals(SpeedStatus.RUNNING.name(), RTContract.ACTIVITY_RUNNING);
        assertEquals(SpeedStatus.CYCLING.name(), RTContract.ACTIVITY_CYCLING);
        assertEquals(SpeedStatus.DRIVING.name(), RTContract.ACTIVITY_DRIVING);

    }
}