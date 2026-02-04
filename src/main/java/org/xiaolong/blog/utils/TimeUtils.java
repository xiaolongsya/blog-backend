package org.xiaolong.blog.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class TimeUtils {
    // 定义全局统一时区（中国上海）
    public static final ZoneId CHINA_ZONE = ZoneId.of("Asia/Shanghai");

    // 优化：获取「当前时间所在小时」的起止时间（统一时区+统一now，无偏差无临界值）
    public static LocalDateTime[] getCurrentHourTimeRange() {
        // 明确指定中国时区获取当前时间，解决8小时偏差
        LocalDateTime now = LocalDateTime.now(CHINA_ZONE);
        // 小时开始时间：当前小时的0分0秒0毫秒（截断到小时，无精度冗余）
        LocalDateTime hourStartTime = now.truncatedTo(ChronoUnit.HOURS);
        // 小时结束时间：当前小时的59分59秒999毫秒（适配数据库，无纳秒冗余）
        LocalDateTime hourEndTime = hourStartTime.plusHours(1)
                .minusNanos(1)
                .truncatedTo(ChronoUnit.MILLIS);

        return new LocalDateTime[]{hourStartTime, hourEndTime};
    }

    // （可选）近1小时内的时间（更合理的频率限制，同样指定时区）
    public static LocalDateTime getOneHourAgoTime() {
        return LocalDateTime.now(CHINA_ZONE)
                .minusHours(1)
                .truncatedTo(ChronoUnit.MILLIS);
    }
}