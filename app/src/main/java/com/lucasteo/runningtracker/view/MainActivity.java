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

    //region VARIABLES
    //--------------------------------------------------------------------------------------------//

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

    //--------------------------------------------------------------------------------------------//
    //endregion

    //region LIFE CYCLE & EVENT
    //--------------------------------------------------------------------------------------------//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // instantiate view model
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // request permission if needed
        if (requestPermission()){
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

    //--------------------------------------------------------------------------------------------//
    //endregion

    //region PERMISSIONS
    //--------------------------------------------------------------------------------------------//

    /**
     * request all listed permissions
     *
     * @return true when all permission is granted
     */
    private boolean requestPermission(){

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
    public static boolean notGrantedPermissions(Context context, String... permissions) {
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

    //--------------------------------------------------------------------------------------------//
    //endregion

    //region SERVICE
    //--------------------------------------------------------------------------------------------//

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
     * run service task
     */
    public void runTrackerService(){
        trackerServiceBinder.runTrackerService();
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

        @Override
        public void onStopMovingUpdateEvent(boolean value) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    viewModel.setValueStopMoving(value);
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

            viewModel.setValueServiceStatus(trackerServiceBinder.getTrackerServiceIsRunning()); // as service existed change status to started
            viewModel.setValueStopMoving(trackerServiceBinder.getTrackerServiceStopMoving());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected: MainActivity");
            trackerServiceBinder.unregisterCallback(callback);
            trackerServiceBinder = null;
        }
    };

    //--------------------------------------------------------------------------------------------//
    //endregion
}
