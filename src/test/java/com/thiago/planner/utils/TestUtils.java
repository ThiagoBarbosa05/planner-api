package com.thiago.planner.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

public class TestUtils {
    public static String objectToJSON(Object obj) {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static UUID generateRandomUUID() {
        return UUID.randomUUID();
    }
}
