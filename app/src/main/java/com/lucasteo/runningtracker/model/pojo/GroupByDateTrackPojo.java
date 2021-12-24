package com.lucasteo.runningtracker.model.pojo;

/**
 * plain old java object
 * storing queried data into this object
 */
public class GroupByDateTrackPojo {
    private int number_of_records;
    private double total_distance;
    private double average_speed;
    private double maximum_speed;
    private String record_date;

    public int getNumber_of_records() {
        return number_of_records;
    }

    public double getTotal_distance() {
        return total_distance;
    }

    public double getAverage_speed() {
        return average_speed;
    }

    public double getMaximum_speed() {
        return maximum_speed;
    }

    public String getRecord_date() {
        return record_date;
    }

    public void setNumber_of_records(int number_of_records) {
        this.number_of_records = number_of_records;
    }

    public void setTotal_distance(double total_distance) {
        this.total_distance = total_distance;
    }

    public void setAverage_speed(double average_speed) {
        this.average_speed = average_speed;
    }

    public void setRecord_date(String record_date) {
        this.record_date = record_date;
    }

    public void setMaximum_speed(double maximum_speed) {
        this.maximum_speed = maximum_speed;
    }
}
