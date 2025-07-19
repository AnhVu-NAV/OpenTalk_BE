package sba301.java.opentalk.utils;

import java.time.LocalDateTime;

public class ConverCron {
    public static String convertCron(LocalDateTime dateTime) {
        int second = dateTime.getSecond();
        int minute = dateTime.getMinute();
        int hour = dateTime.getHour();
        int dayOfMonth = dateTime.getDayOfMonth();
        int month = dateTime.getMonthValue();
        return String.format("%d %d %d %d %d ?",
                second, minute, hour, dayOfMonth, month);
    }
}
