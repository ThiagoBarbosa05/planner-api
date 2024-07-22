package com.thiago.planner.link.dto;

import java.util.UUID;

public record ListLinkResponseDTO(
        UUID id,
        String title,
        String url
) {
}
