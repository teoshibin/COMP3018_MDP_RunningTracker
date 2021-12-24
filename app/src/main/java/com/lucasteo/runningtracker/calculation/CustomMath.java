package com.lucasteo.runningtracker.calculation;

/**
 * custom math class for extra handy math methods
 */
public class CustomMath {

    /**
     * round value with specified decimal point digits
     *
     * @param value value to be rounded
     * @param decimalPoint decimal point digits
     * @return rounded value
     */
    public static double round(double value, int decimalPoint){
        double scale = Math.pow(10, decimalPoint);
        return (double) (Math.round(value * scale) / scale);
    }

}
