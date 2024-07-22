package com.thiago.planner.participant.dto;

import com.thiago.planner.utils.ValidEmailList;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record InviteParticipantsDTO(

        @ValidEmailList(message = "Por favor insira emails válidos.")
        List<String> emails
) {
}
