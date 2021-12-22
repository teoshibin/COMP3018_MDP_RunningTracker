package com.lucasteo.runningtracker.service;

import com.lucasteo.runningtracker.calculation.SpeedStatus;

public interface ICallback {
//    void speedStatusUpdate(SpeedStatus status);
    void onStopMovingUpdateEvent(boolean value);
}
