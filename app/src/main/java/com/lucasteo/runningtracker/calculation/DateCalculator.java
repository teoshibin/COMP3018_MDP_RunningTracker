package com.lucasteo.runningtracker.calculation;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateCalculator {

    public Date minusDays(Date date, int days){
        LocalDateTime ldt = dateToLocalDateTime(date);
        return localDateTimeToDate(ldt.minusDays(days));
    }

    public Date minusMonths(Date date, int months){
        LocalDateTime ldt = dateToLocalDateTime(date);
        return localDateTimeToDate(ldt.minusMonths(months));
    }

    public Date minusWeeks(Date date, int weeks){
        LocalDateTime ldt = dateToLocalDateTime(date);
        return localDateTimeToDate(ldt.minusWeeks(weeks));
    }

    public Date minusYears(Date date, int years){
        LocalDateTime ldt = dateToLocalDateTime(date);
        return localDateTimeToDate(ldt.minusYears(years));
    }

    public LocalDateTime dateToLocalDateTime(Date date){
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public Date localDateTimeToDate(LocalDateTime ldt){
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

}
