package com.thiago.planner.participant.dto;

import java.util.UUID;

public record ParticipantListResponseDTO(
   UUID id,
   String name,
   String email,
   Boolean isConfirmed
) {}
