package com.lucasteo.runningtracker.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.content.Context;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lucasteo.runningtracker.R;
import com.lucasteo.runningtracker.service.ICallback;
import com.lucasteo.runningtracker.service.TrackerService;
import com.lucasteo.runningtracker.view_model.MainViewModel;

import java.util.Objects;

/**
 * main activity
 */
public class MainActivity extends AppCompatActivity {

    //==============================================================================================
    // variables
    //==============================================================================================

    //region

    // log
    private static final String TAG = "runningTracker";

    // permissions
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
    }; // permissions to be requested

    // UI Components
    BottomNavigationView bottomNavigationView;

    // main
    private MainViewModel viewModel;

    // service
    private TrackerService.TrackerServiceBinder trackerServiceBinder;

    //endregion

    //==============================================================================================
    // life cycle
    //==============================================================================================

    //region

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // instantiate view model
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // request permission if needed
        if (requestPermissions()){
            startTrackerService();
        }

        // bottom navigation menu and navigation
        bottomNavigationView = findViewById(R.id.bottomNav);
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // action bar name sync with fragment names
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(R.id.home2, R.id.stats).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

    }

    //endregion

    //==============================================================================================
    // permissions
    //==============================================================================================

    //region

    /**
     * request all listed permissions
     *
     * @return true when all permission is granted
     */
    public boolean requestPermissions(){

        boolean allGranted = true;

        if (notGrantedPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
            allGranted = false;
        }
        return allGranted;
    }

    /**
     * check if any of the permission is not given
     *
     * @param context context
     * @param permissions list of permissions
     * @return false when all permissions are given
     */
    private static boolean notGrantedPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // making sure every permission is granted
        boolean allGranted = true;
        if(notGrantedPermissions(this, PERMISSIONS)){
            allGranted = false;
        }

        // start service if all permission is granted
        if(allGranted){
            startTrackerService();
        }
    }

    //endregion

    //==============================================================================================
    // service
    //==============================================================================================

    //region

    /**
     * start service
     */
    public void startTrackerService(){
        this.startForegroundService(
                new Intent(this, TrackerService.class));
        this.bindService(
                new Intent(this, TrackerService.class),
                serviceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * run the started tracker service
     *
     * @return true is succesfully run the service else false as permission is not granted
     */
    public boolean runTrackerService(){
        if (requestPermissions()){
            trackerServiceBinder.runTrackerService();
            return true;
        } else {
            Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    /**
     * stop running service task
     */
    public void stopTrackerService(){
        trackerServiceBinder.pauseTrackerService();
    }

    /**
     * define concrete call back
     */
    private final ICallback callback = new ICallback() {

        /**
         * tell the UI that user has stop moving
         * the reason why this is required is because standing data is not saved as actual rows
         * in database meaning cannot be observed directly from LiveData of the database
         *
         * @param value stop moving boolean
         */
        @Override
        public void onStopMovingUpdateEvent(boolean value) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    viewModel.setValueStopMoving(value);
                }
            });
        }

        /**
         * impossible to reach this part of the code as checking is always done in advance
         * but who knows nothing is impossible in coding land
         *
         * service will cry for permission as permission is still not granted
         */
        @Override
        public void onPermissionNotGranted() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    requestPermissions();
                }
            });
        }

    };

    /**
     * define service connection behaviour
     */
    private final ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.d(TAG, "onServiceConnected: MainActivity");
            trackerServiceBinder = (TrackerService.TrackerServiceBinder) binder;
            trackerServiceBinder.registerCallback(callback);

            // service started change UI service status to current service status
            viewModel.setValueServiceStatus(trackerServiceBinder.getTrackerServiceIsRunning());
            // service started change UI stop moving status to current service stop moving status
            viewModel.setValueStopMoving(trackerServiceBinder.getTrackerServiceStopMoving());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected: MainActivity");
            trackerServiceBinder.unregisterCallback(callback);
            trackerServiceBinder = null;
        }
    };

    //endregion
}
