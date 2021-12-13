package com.lucasteo.runningtracker;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class MyLocationListener implements LocationListener {

//    private Location prevLocation = null;
//    float distance = 0;

    @Override
    public void onLocationChanged(Location location) {
//        Log.d("comp3018", location.getLatitude() + " " + location.getLongitude());
//        if(prevLocation == null){
//            prevLocation = location;
//        } else {
//            distance = prevLocation.distanceTo(location);
//            prevLocation = location;
//            Log.d("comp3018", "onLocationChanged: " + distance);
//        }

        Log.d("comp3018", location.toString());
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
// information about the signal, i.e. number of satellites
        Log.d("comp3018", "onStatusChanged: " + provider + " " + status);
    }
    @Override
    public void onProviderEnabled(String provider) {
// the user enabled (for example) the GPS
        Log.d("comp3018", "onProviderEnabled: " + provider);
    }
    @Override
    public void onProviderDisabled(String provider) {
// the user disabled (for example) the GPS
        Log.d("comp3018", "onProviderDisabled: " + provider);
    }
}
