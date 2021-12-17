package com.lucasteo.runningtracker.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lucasteo.runningtracker.R;
import com.lucasteo.runningtracker.service.ICallback;
import com.lucasteo.runningtracker.service.TrackerService;
import com.lucasteo.runningtracker.viewmodel.MainViewModel;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    //region VARIABLES
    //--------------------------------------------------------------------------------------------//

    // log
    private final String TAG = "MainActivity";

    // permissions
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    // UI Components
//    Button mainBtn;
    BottomNavigationView bottomNavigationView;

    // main
    private MainViewModel viewModel;
    private boolean serviceStarted = false;

    //--------------------------------------------------------------------------------------------//
    //endregion

    //region LIFE CYCLE & EVENT
    //--------------------------------------------------------------------------------------------//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // instantiate view model
        viewModel =
                new ViewModelProvider(this,
                        ViewModelProvider
                                .AndroidViewModelFactory
                                .getInstance(this.getApplication())
                ).get(MainViewModel.class);

        // request permission if needed
        requestPermission();

        // Find UI Components
//        mainBtn = findViewById(R.id.button);

        // bottom navigation menu and navigation
        bottomNavigationView = findViewById(R.id.bottomNav);
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // action bar name sync with fragment names
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.home2, R.id.stats).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

    }

//    public void btnOnClick(View view) {
//
//        // switch button display text and controlling service
//        if(requestPermission()){
//            if(serviceStarted){
//                mainBtn.setText(R.string.btnStart);
//                StopTrackerService();
//            } else {
//                mainBtn.setText(R.string.btnStop);
//                startTrackerService();
//            }
//            serviceStarted = !serviceStarted;
//        }
//    }

    //--------------------------------------------------------------------------------------------//
    //endregion

    //region Tracker Service
    //--------------------------------------------------------------------------------------------//

//    private void startTrackerService(){
//        this.startForegroundService(new Intent(this, TrackerService.class));
//        this.bindService(new Intent(this, TrackerService.class),
//                serviceConnection, Context.BIND_AUTO_CREATE);
//    }
//
//    private void StopTrackerService(){
//        trackerServiceBinder.stopTrackerService();
//    }
//
//    ICallback callback = new ICallback() {
//        // to use this remember to use runOnUiThread new Runnable()
//    };
//
//    private final ServiceConnection serviceConnection = new ServiceConnection() {
//
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder binder) {
//            Log.d(TAG, "onServiceConnected: MainActivity");
//            trackerServiceBinder = (TrackerService.TrackerServiceBinder) binder;
//            trackerServiceBinder.registerCallback(callback);
//            // TODO do something on service connect
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            Log.d(TAG, "onServiceDisconnected: MainActivity");
//            trackerServiceBinder.unregisterCallback(callback);
//            trackerServiceBinder = null;
//        }
//    };
    //--------------------------------------------------------------------------------------------//
    //endregion

    //region PERMISSIONS
    //--------------------------------------------------------------------------------------------//

    private boolean requestPermission(){

        boolean allGranted = true;

        if (noPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
            allGranted = false;
        }
        return allGranted;
    }

    public static boolean noPermissions(Context context, String... permissions) {
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
//        boolean allGranted = true;
//        if(!hasPermissions(this, PERMISSIONS)){
//            allGranted = false;
//        }

//        // start service if all permission is granted
//        if(allGranted = true){
//            startTrackerService();
//        }
    }

    //--------------------------------------------------------------------------------------------//
    //endregion
}
