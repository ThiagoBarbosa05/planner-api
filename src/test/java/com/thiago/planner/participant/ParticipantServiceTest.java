package com.thiago.planner.participant;

import com.thiago.planner.participant.dto.ParticipantListResponseDTO;
import com.thiago.planner.participant.dto.RegisterParticipantsDTO;
import com.thiago.planner.trip.Trip;
import com.thiago.planner.trip.dto.CreateTripDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ParticipantServiceTest {

    @Mock
    private ParticipantRepository participantRepository;

    @InjectMocks
    private ParticipantService participantService;

    @Test
    @DisplayName("Should to register participants to a trip")
    void shouldRegisterParticipantsToTrip() {
        CreateTripDTO createTripDTO = new CreateTripDTO(
                "TEST user",
                "test@email.com",
                List.of("invite1@email.com", "invite2@email.com"),
                "statue of liberty, USA",
                false,
                LocalDateTime.now().plusDays(2).toString(),
                LocalDateTime.now().plusDays(5).toString()
        );
        Trip trip = new Trip(createTripDTO);
        List<String> emails = new ArrayList<>();
        emails.add("test@email.com");
        emails.add("test2@email.com");
        RegisterParticipantsDTO registerParticipantsDTO = new RegisterParticipantsDTO(emails, trip);
        List<Participant> participants = registerParticipantsDTO
                .participantEmailsToInvite()
                .stream()
                .map(email -> new Participant(email, registerParticipantsDTO.trip())).toList();


        when(this.participantRepository.saveAll(anyList())).thenReturn(participants);

        List<Participant> result = participantService.registerParticipantsToTrip(registerParticipantsDTO);

        assertNotNull(result);
        assertEquals(result, participants);
        assertEquals(2, result.size());
        verify(participantRepository, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("Should list all trip participants")
    void shouldListAllTripParticipants() {
        List<Participant> participants = List.of(
                new Participant(),
                new Participant()
        );

        UUID tripId = UUID.randomUUID();

        when(participantRepository.findAllByTripId(tripId)).thenReturn(participants);

        List<ParticipantListResponseDTO> result = this.participantService.getAllParticipantsFromTrip(tripId);

        assertNotNull(result);
        verify(participantRepository, times(1)).findAllByTripId(tripId);
    }
}