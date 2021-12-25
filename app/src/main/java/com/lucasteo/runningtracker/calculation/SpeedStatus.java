package com.lucasteo.runningtracker.calculation;

/**
 * speed status class for classifying different speed into different categories of activities that
 * the user might be doing
 */
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

    // this is used as a modifier for scaling the threshold
    // due to inaccurate speed from GPS or emulator
    private double scale = 1.0;

    private double rawThreshold;
    private double scaledThreshold;

    /**
     * enum constructor to assign each activities' threshold values
     *
     * @param rawThreshold threshold
     */
    SpeedStatus(double rawThreshold){
        this.rawThreshold = rawThreshold;
        this.scaledThreshold = scale * rawThreshold;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public double getRawThreshold() {
        return rawThreshold;
    }

    public void setRawThreshold(double rawThreshold) {
        this.rawThreshold = rawThreshold;
    }

    public double getScaledThreshold() {
        return scaledThreshold;
    }

    public void setScaledThreshold(double scaledThreshold) {
        this.scaledThreshold = scaledThreshold;
    }

    /**
     * classify input speed into one of the speedStatus enum
     *
     * @param speed speed of type double
     * @return SpeedStatus
     */
    public static SpeedStatus classifyWithScaledThreshold(double speed){
        double absSpeed = Math.abs(speed);
        if (absSpeed >= DRIVING.getScaledThreshold()){
            return DRIVING;
        }
        if (absSpeed >= CYCLING.getScaledThreshold()){
            return CYCLING;
        }
        if (absSpeed >= RUNNING.getScaledThreshold()){
            return RUNNING;
        }
        if (absSpeed >= JOGGING.getScaledThreshold()){
            return JOGGING;
        }
        if (absSpeed >= WALKING.getScaledThreshold()){
            return WALKING;
        }
        return STANDING;
    }
}
