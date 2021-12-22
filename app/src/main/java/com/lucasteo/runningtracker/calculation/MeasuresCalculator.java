package com.lucasteo.runningtracker.calculation;

public class MeasuresCalculator {

    // auxiliary function
    private double invertibleScaling(double value, double scale, boolean invert){
        return invert ? value / scale : value * scale;
    }

    // conversion between imperial and metrics

    public double inchesToCentimeters(double inchesOrCentimeters, boolean invert){
        return invertibleScaling(inchesOrCentimeters, 2.54, invert);
    }

    public double feetToCentimeters(double feetOrCentimeters, boolean invert){
        return invertibleScaling(feetOrCentimeters, 30.48, invert);
    }

    public double yardsToMeters(double yardsOrMeters, boolean invert){
        return invertibleScaling(yardsOrMeters, 0.9144, invert);
    }

    public double milesToKilometers(double milesOrKilometers, boolean invert){
        return invertibleScaling(milesOrKilometers, 1.60934, invert);
    }

    // imperial

    public double milesToYards(double milesOrYards, boolean invert){
        return invertibleScaling(milesOrYards, 1760, invert);
    }

    public double yardToFeet(double yardOrFeet, boolean invert){
        return invertibleScaling(yardOrFeet, 3, invert);
    }

    public double feetToInches(double feetOrInches, boolean invert){
        return invertibleScaling(feetOrInches, 12, invert);
    }

    // SI

    public double kilometersToMeters(double kilometersOrMeters, boolean invert){
        return invertibleScaling(kilometersOrMeters, 1000, invert);
    }

    public double metersToCentimeters(double metersOrCentimeters, boolean invert){
        return invertibleScaling(metersOrCentimeters, 100, invert);
    }
}
