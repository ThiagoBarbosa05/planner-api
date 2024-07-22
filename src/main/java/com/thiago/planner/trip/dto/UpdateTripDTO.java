package com.thiago.planner.trip.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UpdateTripDTO(
        @NotBlank(message = "Insira um destino para a viagem.")
        @Schema(example = "New York, USA")
        String destination,

        @NotBlank(message = "Insira uma data de início da viagem.")
        @Schema(example = "2024-07-24T21:51:54.7342")
        String startsAt,

        @NotBlank(message = "Insira uma data de Término da viagem.")
        @Schema(example = "2024-07-24T21:51:54.7342")
        String endsAt
) {
}
