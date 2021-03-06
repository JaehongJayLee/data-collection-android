package com.hongdoki.datacollection.util;

public class TimeUnitUtil {
    private static final long A_SECOND_IN_MILLIS = 1000;
    private static final long A_MINUTE_IN_MILLIS = 60 * A_SECOND_IN_MILLIS;
    private static final long AN_HOUR_IN_MILLIS = 60 * A_MINUTE_IN_MILLIS;

    public static long millisToHour(long millis) {
        return millis / AN_HOUR_IN_MILLIS;
    }

    public static long frequencyToPeriodMillis(long frequencyInHz) {
        return A_SECOND_IN_MILLIS/frequencyInHz;
    }

    public static long minuteToMillis(long minute) {
        return minute * A_MINUTE_IN_MILLIS;
    }

    public static long secondToMillis(long seconds) {
        return seconds * A_SECOND_IN_MILLIS;
    }
}
