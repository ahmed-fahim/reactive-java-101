package com.ahmedfahim.reactivejava101.utils;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateTimeUtils {
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss:SSS Z";
    private static final String LOCAL_ZONE_ID = "Asia/Dhaka";

    private final DateTimeFormatter dateTimeFormatter;

    public DateTimeUtils() {
        this.dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
    }

    public ZonedDateTime getCurrentZonedDateTime() {
        return ZonedDateTime.ofInstant(Instant.now(), ZoneId.of(LOCAL_ZONE_ID));
    }

    public String getCurrentTimestamp() {
        return strTimeStampFromZonedDateTime(this.getCurrentZonedDateTime());
    }

    public ZonedDateTime zonedDateTimeFromStrTimeStamp(String strTimeStamp) {
        return ZonedDateTime.parse(strTimeStamp, this.dateTimeFormatter);
    }

    public String strTimeStampFromZonedDateTime(ZonedDateTime zonedDateTime) {
        return this.dateTimeFormatter.format(zonedDateTime);
    }

}
