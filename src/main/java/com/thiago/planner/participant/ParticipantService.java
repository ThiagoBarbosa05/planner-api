package com.thiago.planner.participant;

import com.thiago.planner.participant.dto.ParticipantListResponseDTO;
import com.thiago.planner.participant.dto.RegisterParticipantsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ParticipantService {

    private final ParticipantRepository participantRepository;

    @Autowired
    public ParticipantService(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    public List<Participant> registerParticipantsToTrip(RegisterParticipantsDTO registerParticipantsDTO) {
        List<Participant> participants = registerParticipantsDTO
                .participantEmailsToInvite()
                .stream()
                .map(email -> new Participant(email, registerParticipantsDTO.trip())).toList();

        return this.participantRepository.saveAll(participants);
    }

    public List<ParticipantListResponseDTO> getAllParticipantsFromTrip(UUID tripId) {
        return this.participantRepository.findAllByTripId(tripId).stream().map(participant -> new ParticipantListResponseDTO(
                participant.getId(),
                participant.getName(),
                participant.getEmail(),
                participant.getIsConfirmed()
        )).toList();
    }
}
