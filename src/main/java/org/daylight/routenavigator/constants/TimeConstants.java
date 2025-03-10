package org.daylight.routenavigator.constants;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Global time constants
 */
public class TimeConstants {
    public static final int timezoneHours = 3;
    public static final ZoneOffset ZONE_OFFSET = ZoneOffset.ofHours(timezoneHours);

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssXXX");
}
