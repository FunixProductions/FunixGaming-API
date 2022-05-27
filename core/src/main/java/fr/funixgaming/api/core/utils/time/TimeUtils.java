package fr.funixgaming.api.core.utils.time;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class TimeUtils {

    public static Duration diffBetweenInstants(final Instant start, final Instant end) {
        return Duration.between(start, end);
    }

    public static long diffInMillisBetweenInstants(final Instant start, final Instant end) {
        return ChronoUnit.MILLIS.between(start, end);
    }

}
