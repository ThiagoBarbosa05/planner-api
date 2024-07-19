package com.thiago.planner.activity.dto;

import java.time.LocalDateTime;

public record CreateActivityDTO(
        String title,
        LocalDateTime occursAt
) {
}
