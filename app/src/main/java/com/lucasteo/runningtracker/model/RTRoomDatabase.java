package com.lucasteo.runningtracker.model;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.lucasteo.runningtracker.calculations.DateCalculator;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Track.class}, version = 1, exportSchema = false)
@TypeConverters({DateTimeConverter.class})
public abstract class RTRoomDatabase extends RoomDatabase {

    // constant
    private final static String TAG = "runningTracker";

    // data access object
    public abstract TrackDao trackDao();

    // repo
    private static volatile RTRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static RTRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RTRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            RTRoomDatabase.class, "track_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(createCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback createCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            Log.d(TAG, "onCreate: RTRoomDatabase");

            // create database using non UI thread
            databaseWriteExecutor.execute(() -> {

                TrackDao trackDao = INSTANCE.trackDao();
                trackDao.deleteAll();

                Date date = new Date();
                DateCalculator dateCalculator = new DateCalculator();
                Date a = dateCalculator.minusDays(date, 1);
                Date b = dateCalculator.minusDays(date, 2);
                Date c = dateCalculator.minusDays(date, 3);
                Date d = dateCalculator.minusDays(date, 4);
                Date e = dateCalculator.minusDays(date, 5);
                Date f = dateCalculator.minusDays(date, 6);
                Date g = dateCalculator.minusDays(date, 7);
                Date h = dateCalculator.minusDays(date, 8);
                Date i = dateCalculator.minusDays(date, 9);
                Log.d(TAG, "onCreate: " + a.toString());

                Track track1 = new Track(0,1,1,1,1,1, a);
                Track track2 = new Track(0,1,1,1,1,1, b);
                Track track3 = new Track(0,1,1,1,1,1, c);
                Track track4 = new Track(0,1,1,1,1,1, d);
                Track track5 = new Track(0,1,1,1,1,1, e);
                Track track6 = new Track(0,1,1,1,1,1, f);
                Track track7 = new Track(0,1,1,1,1,1, g);
                Track track8 = new Track(0,1,1,1,1,1, h);
                Track track9 = new Track(0,1,1,1,1,1, i);
                Log.d(TAG, "onCreate: " + track1.toString());

                trackDao.insert(track1);
                trackDao.insert(track2);
                trackDao.insert(track3);
                trackDao.insert(track4);
                trackDao.insert(track5);
                trackDao.insert(track6);
                trackDao.insert(track7);
                trackDao.insert(track8);
                trackDao.insert(track9);

            });
        }
    };

}
