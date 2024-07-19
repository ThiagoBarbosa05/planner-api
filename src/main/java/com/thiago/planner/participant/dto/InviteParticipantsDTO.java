package com.thiago.planner.participant.dto;

import java.util.List;

public record InviteParticipantsDTO(
        List<String> emails
) {
}
