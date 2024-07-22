package com.thiago.planner.activity.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record CreateActivityDTO(
        @NotBlank
        String title,

        @NotBlank
        String occursAt
) {
}
