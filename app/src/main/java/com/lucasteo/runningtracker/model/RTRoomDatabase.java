package com.lucasteo.runningtracker.model;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Track.class}, version = 1, exportSchema = false) // drop and recreat
public abstract class RTRoomDatabase extends RoomDatabase {

    public abstract TrackDao trackDao();

    private static volatile RTRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static RTRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RTRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
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

            Log.d("comp3018", "dboncreate");

            // create database using non UI thread
            databaseWriteExecutor.execute(() -> {

                TrackDao trackDao = INSTANCE.trackDao();
                trackDao.deleteAll();

                // TODO dynamic ID
                Track track = new Track(0,0);
                trackDao.insert(track);

            });
        }
    };

}
