package com.lucasteo.runningtracker.model;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.lucasteo.runningtracker.model.dao.TrackDao;
import com.lucasteo.runningtracker.model.entity.Track;
import com.lucasteo.runningtracker.model.type_converter.DateTimeConverter;

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
    private static volatile RTRoomDatabase Instance;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * Gets the singleton instance of Running Tracker Room Database
     *
     * @param context context
     * @return The singleton instance of Running Tracker Room Database
     */
    public static RTRoomDatabase getDatabase(final Context context) {
        if (Instance == null) {
            synchronized (RTRoomDatabase.class) {
                if (Instance == null) {
                    Instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            RTRoomDatabase.class, "track_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(createCallback)
                            .build();
                }
            }
        }
        return Instance;
    }

    /**
     * This callback will be called when database version increased and the app is installed for
     * the first time
     */
    private static RoomDatabase.Callback createCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            Log.d(TAG, "onCreate: RTRoomDatabase");

            // create database using non UI thread
            databaseWriteExecutor.execute(() -> {

                TrackDao trackDao = Instance.trackDao();
                trackDao.deleteAll();

            });
        }
    };

    /**
     * Switches the internal implementation with an empty in-memory database.
     *
     * @param context The context.
     */
    @VisibleForTesting
    public static void switchToInMemory(Context context) {
        Instance = Room.inMemoryDatabaseBuilder(context.getApplicationContext(),
                RTRoomDatabase.class).build();
    }

}
