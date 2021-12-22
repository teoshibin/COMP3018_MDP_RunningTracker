package com.lucasteo.runningtracker.calculation;

public class CustomMath {

    public static double round(double value, int decimalPoint){
        double scale = Math.pow(10, decimalPoint);
        return (double) (Math.round(value * scale) / scale);
    }

}
