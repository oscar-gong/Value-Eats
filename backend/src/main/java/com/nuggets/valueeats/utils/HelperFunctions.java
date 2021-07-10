package com.nuggets.valueeats.utils;

import java.time.Duration;
import java.util.Date;

public class HelperFunctions {
    
    public static Long getDuration(Date date, Integer end) {
        Date endTime = Date.from(date.toInstant().plus(Duration.ofMinutes(end)));
        Date timeNow = new Date(System.currentTimeMillis());
        timeNow = Date.from(timeNow.toInstant().plus(Duration.ofHours(10)));

        return Duration.between(timeNow.toInstant(), endTime.toInstant()).toMillis();
    }

    public static boolean checkActive (Date date, Integer end) {
        Date timeNow = new Date(System.currentTimeMillis());
        timeNow = Date.from(timeNow.toInstant().plus(Duration.ofHours(10)));
        Date endTime = Date.from(date.toInstant().plus(Duration.ofMinutes(end)));;
        return (endTime.compareTo(timeNow) > 0);
    }

    public static boolean isInTimeRange(Date date, Integer start, Integer end) {
        Date timeNow = new Date(System.currentTimeMillis());
        timeNow = Date.from(timeNow.toInstant().plus(Duration.ofHours(10)));
        Date startTime = Date.from(date.toInstant().plus(Duration.ofMinutes(start)));
        Date endTime = Date.from(date.toInstant().plus(Duration.ofMinutes(end)));
        // Check if start time before timeNow, endTime after timeNow
        if (startTime.compareTo(timeNow) <= 0 && endTime.compareTo(timeNow) > 0) {
            return true;
        }
        return false;
    }

}
