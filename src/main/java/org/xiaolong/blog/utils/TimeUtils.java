package org.xiaolong.blog.utils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;


public class TimeUtils {
    public static LocalDateTime getCurrentHourStartTime() {
        LocalDateTime now = LocalDateTime.now();
        return now.with(LocalTime.of(now.getHour(), 0, 0, 0));
    }

    public static LocalDateTime getCurrentHourEndTime() {
        LocalDateTime now = LocalDateTime.now();
        return now.with(LocalTime.of(now.getHour(), 59, 59, 999_999_999));
    }
}