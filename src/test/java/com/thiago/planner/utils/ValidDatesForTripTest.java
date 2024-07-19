package com.thiago.planner.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ValidDatesForTripTest {

    @Test
    void checkIfCurrentDateIsEqualOrAfterStartsDate() {
        LocalDateTime startsDate = LocalDateTime.now().plusDays(2);

        Boolean result = ValidDatesForTrip.isCurrentDateEqualOrAfterStartsDate(startsDate);

        assertEquals(result, false);
    }

    @Test
    void checkIfEndsDateIsBeforeStartsDate() {
        LocalDateTime startsDate = LocalDateTime.now().plusDays(2);
        LocalDateTime endsDate = LocalDateTime.now().plusDays(3);

        Boolean result = ValidDatesForTrip.isEndsDateBeforeStartsDate(startsDate, endsDate);

        assertEquals(result, false);
    }
}