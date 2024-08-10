package com.thiago.planner.trip.dto;

import com.thiago.planner.utils.ValidEmailList;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.List;

public record CreateTripDTO(

        @NotBlank(message = "É preciso informar um nome para esse campo.")
        @Schema(example = "John Doe")
        String ownerName,

        @NotBlank(message = "É preciso informar um email para esse campo.")
        @Email(message = "Por favor insira um email válido.")
        @Schema(example = "john@doe.com")
        String ownerEmail,

        @ValidEmailList(message = "Por favor insira emails válidos.")
        List<String> emailsToInvite,

        @NotBlank(message = "Insira um destino para a viagem.")
        @Schema(example = "Cristo Redentor")
        String destination,

        Boolean isConfirmed,

        @NotBlank(message = "Insira uma data de início da viagem.")
        @Schema(example = "2024-07-30T21:51:54.7342")

        String startsAt,

        @NotBlank(message = "Insira uma data de Término da viagem.")
        @Schema(example = "2024-07-30T21:51:54.7342")
        String endsAt
) {}
