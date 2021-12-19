package com.lucasteo.runningtracker.calculations;

public enum SpeedStatus {

    // derived from multiple statistics of speed across genders age etc.
    // avg speed (m/s) of each activity
    // standing 0       threshold value = (0 + 1.095)/2 try it yourself in excel
    // walking 1.095
    // jogging 2.2
    // running 3.2
    // cycling 6.7
    // driving slow 13
    // driving fast 30
    // train 50
    // airplane 250

    STANDING(0),
    WALKING(0.5),
    JOGGING(1.65),
    RUNNING(2.7),
    CYCLING(4.95),
    DRIVING(9.85);

    // this is used as a modifier for threshold due to inaccurate speed from GPS or emulator
    private static final double scale = 1.0;

    private double rawThreshold;
    private double scaledThreshold;

    SpeedStatus(double rawThreshold){
        this.rawThreshold = rawThreshold;
        this.scaledThreshold = scale * rawThreshold;
    }

}
