package com.thiago.planner.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ValidDatesForTrip {

    // Change this
    public static Boolean isCurrentDateEqualOrAfterStartsDate(LocalDateTime startDate) {
        LocalDateTime currentDate = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        return currentDate.isEqual(startDate.truncatedTo(ChronoUnit.DAYS)) || currentDate.isAfter(startDate.truncatedTo(ChronoUnit.DAYS));
    }

    public static Boolean isEndsDateBeforeStartsDate(LocalDateTime startDate, LocalDateTime endsDate) {
        return endsDate.isBefore(startDate);
    }
}

