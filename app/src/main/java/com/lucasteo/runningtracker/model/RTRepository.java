package com.lucasteo.runningtracker.model;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.lucasteo.runningtracker.model.dao.TrackDao;
import com.lucasteo.runningtracker.model.entity.Track;
import com.lucasteo.runningtracker.model.pojo.GroupByDateTrackPojo;

import java.util.List;

public class RTRepository {

    // DAO
    private TrackDao trackDao;

    // Data
    private LiveData<List<Track>> allTracks;
//    private LiveData<List<Track>> queriedTracks;
    private LiveData<List<GroupByDateTrackPojo>> allGroupByDateTracks;

    /**
     * repository constructor
     * initiate all data from db into repo using application as reference
     * @param application reference to database
     */
    public RTRepository(Application application) {
        RTRoomDatabase db = RTRoomDatabase.getDatabase(application);

        // retrieving tracks dao
        trackDao = db.trackDao();

        // initialize data
        allTracks = trackDao.getTracks();
        allGroupByDateTracks = trackDao.getGroupByDateTracks();


//        queriedTracks = loadTracksWithDateRange(DateRange.TODAY);


//        RTRoomDatabase.databaseWriteExecutor.execute(() -> {
//             statisticPojo = trackDao.getTracksStatistics();
//        });


//        Log.d("runningTracker", "RTRepository: " + statisticPojo);

    }

//    public enum DateRange{
//        PAST_ONE_DAY,
//        PAST_ONE_WEEK,
//        PAST_ONE_MONTH,
//        PAST_ONE_YEAR,
//        TODAY,
//        YESTERDAY,
//        THIS_WEEK_STARTING_FROM_SUNDAY,
//        THIS_WEEK_STARTING_FROM_MONDAY,
//        THIS_MONTH,
//        THIS_YEAR,
//    }

    // standard queries

    public void insert(Track track){
        RTRoomDatabase.databaseWriteExecutor.execute(() -> {
            trackDao.insert(track);
        });
    }

    public void deleteAll(){
        RTRoomDatabase.databaseWriteExecutor.execute(() -> {
            trackDao.deleteAll();
        });
    }

//    public LiveData<List<Track>> loadTracksWithDateRange(DateRange dateRange){
//        LiveData<List<Track>> temp = null;
//        switch (dateRange){
//            case PAST_ONE_DAY:
//                temp = trackDao.getTracksPastOneDay();
//                break;
//            case PAST_ONE_WEEK:
//                temp = trackDao.getTracksPastOneWeek();
//                break;
//            case PAST_ONE_MONTH:
//                temp = trackDao.getTracksPastOneMonth();
//                break;
//            case PAST_ONE_YEAR:
//                temp = trackDao.getTracksPastOneYear();
//                break;
//            case TODAY:
//                temp = trackDao.getTracksToday();
//                break;
//            case YESTERDAY:
//                temp = trackDao.getTracksYesterday();
//                break;
//            case THIS_WEEK_STARTING_FROM_SUNDAY:
//                temp = trackDao.getTracksThisWeekSUN();
//                break;
//            case THIS_WEEK_STARTING_FROM_MONDAY:
//                temp = trackDao.getTracksThisWeekMON();
//                break;
//            case THIS_MONTH:
//                temp = trackDao.getTracksThisMonth();
//                break;
//            case THIS_YEAR:
//                temp = trackDao.getTracksThisYear();
//                break;
//        }
//        return temp;
//    }

    // setter getter

    public LiveData<List<Track>> getAllTracks() {
        return allTracks;
    }

    public LiveData<List<GroupByDateTrackPojo>> getAllGroupByDateTracks(){
        return allGroupByDateTracks;
    }

//    public LiveData<List<Track>> getQueriedTracks(){
//        return queriedTracks;
//    }



}
