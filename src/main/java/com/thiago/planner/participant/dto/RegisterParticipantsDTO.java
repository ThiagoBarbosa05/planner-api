package com.thiago.planner.participant.dto;

import com.thiago.planner.trip.Trip;

import java.util.List;

public record RegisterParticipantsDTO(
        List<String> participantEmailsToInvite,
        Trip trip
    ) {}
