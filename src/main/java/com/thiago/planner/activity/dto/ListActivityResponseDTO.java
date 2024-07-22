package com.thiago.planner.activity.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ListActivityResponseDTO(
        UUID id,
        String title,
        LocalDateTime occursAt
) {
}
