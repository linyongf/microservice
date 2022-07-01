package com.core.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;

/**
 * @Description 日期时间工具类
 * @Author linyf
 * @Date 2022-06-24 16:31
 */
public class DateUtil {

    /**
     * yyyy-mm-dd
     */
    public static final String YYYY_MM_DD = "yyyy-mm-dd";

    /**
     * @Description date 转 LocalDateTime
     * @param: date
     * @return: java.time.LocalDateTime
     * @Author linyf
     * @Date 2022-06-24 16:31
     */
    public static LocalDateTime date2LocalDateTime(Date date){
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * @Description 获取两个时间的时间差，返回秒
     * @param: startTime
     * @param: endTime
     * @return: java.lang.Long
     * @Author linyf
     * @Date 2022-06-24 16:32
     */
    public static Long between(LocalDateTime startTime, LocalDateTime endTime){
        return Duration.between(startTime, endTime).get(ChronoUnit.SECONDS);
    }

    /**
     * @Description  获取两个时间的时间差
     * @param: startTime
     * @param: endTime
     * @param: temporalUnit {@link TemporalUnit}
     * @return: java.lang.Long
     * @Author linyf
     * @Date 2022-06-24 16:32
     */
    public static Long between(LocalDateTime startTime, LocalDateTime endTime, TemporalUnit temporalUnit){
        return Duration.between(startTime, endTime).get(temporalUnit);
    }
}
