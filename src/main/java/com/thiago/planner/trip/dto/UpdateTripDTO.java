package com.thiago.planner.trip.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateTripDTO(
        @NotBlank(message = "Insira um destino para a viagem.")
        String destination,

        @NotBlank(message = "Insira uma data de início da viagem.")
        String startsAt,

        @NotBlank(message = "Insira uma data de Término da viagem.")
        String endsAt
) {
}
