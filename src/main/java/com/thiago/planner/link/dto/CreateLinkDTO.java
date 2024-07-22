package com.thiago.planner.link.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateLinkDTO(
        @NotBlank
        String title,

        @NotBlank
        String url
) {
}
