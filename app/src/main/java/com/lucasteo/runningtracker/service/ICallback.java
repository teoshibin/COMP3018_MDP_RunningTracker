package com.lucasteo.runningtracker.service;

import android.location.Location;
import android.os.Bundle;

public interface ICallback {
    void speedStatusUpdate(SpeedStatus status);
}
