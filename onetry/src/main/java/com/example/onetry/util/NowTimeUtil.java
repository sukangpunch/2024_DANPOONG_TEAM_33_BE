package com.example.onetry.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class NowTimeUtil {
    public static LocalDateTime timeNowInZone(String zoneId){
        return ZonedDateTime.now(ZoneId.of(zoneId)).toLocalDateTime();
    }
}
