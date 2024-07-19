package com.thiago.planner.trip.dto;

import com.thiago.planner.utils.ValidEmailList;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CreateTripDTO(

        @NotBlank(message = "É preciso informar um nome para esse campo.")
        String ownerName,

        @NotBlank(message = "É preciso informar um email para esse campo.")
        @Email(message = "Por favor insira um email válido.")
        String ownerEmail,

        @ValidEmailList(message = "Por favor insira emails válidos.")
        List<String> emailsToInvite,

        @NotBlank(message = "Insira um destino para a viagem.")
        String destination,

        Boolean isConfirmed,

        @NotBlank(message = "Insira uma data de início da viagem.")
        String startsAt,

        @NotBlank(message = "Insira uma data de Término da viagem.")
        String endsAt
) {}
