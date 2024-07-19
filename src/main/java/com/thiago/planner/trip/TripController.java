package com.thiago.planner.trip;


import com.thiago.planner.activity.ActivityService;
import com.thiago.planner.activity.dto.CreateActivityDTO;
import com.thiago.planner.participant.Participant;
import com.thiago.planner.participant.ParticipantService;
import com.thiago.planner.participant.dto.InviteParticipantsDTO;
import com.thiago.planner.participant.dto.ParticipantListResponseDTO;
import com.thiago.planner.participant.dto.RegisterParticipantsDTO;
import com.thiago.planner.trip.dto.CreateTripDTO;
import com.thiago.planner.trip.dto.UpdateTripDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/trip")
public class TripController {

    private final TripService tripService;
    private final ParticipantService participantService;
    private final ActivityService activityService;

    @Autowired
    public TripController(
            TripService tripService,
            ParticipantService participantService,
            ActivityService activityService
    ) {
        this.tripService = tripService;
        this.participantService = participantService;
        this.activityService = activityService;
    }

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody CreateTripDTO createTripDTO) {
        try {
            Trip result = this.tripService.createTrip(createTripDTO);

            RegisterParticipantsDTO registerParticipantsDTO = new RegisterParticipantsDTO(createTripDTO.emailsToInvite(), result);

            participantService.registerParticipantsToTrip(registerParticipantsDTO);

            return ResponseEntity.status(201).body(result);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@Valid @RequestBody UpdateTripDTO updateTripDTO, @PathVariable String id) {
        try {
            Trip result = this.tripService.updateTrip(updateTripDTO, UUID.fromString(id));

            return ResponseEntity.ok().body(result);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ParticipantListResponseDTO>> getAllParticipants(@PathVariable String id) {
        List<ParticipantListResponseDTO> participants = this.participantService.getAllParticipantsFromTrip(UUID.fromString(id));

        return ResponseEntity.ok().body(participants);
    }

    @PostMapping("/{id}/invite")
    public ResponseEntity<Object> inviteParticipants(@RequestBody InviteParticipantsDTO inviteParticipantsDTO, @PathVariable String id) {
        Trip trip = this.tripService.getTrip(UUID.fromString(id));

        RegisterParticipantsDTO registerParticipantsDTO = new RegisterParticipantsDTO(
                inviteParticipantsDTO.emails(),
                trip
        );

        List<Participant> result = this.participantService.registerParticipantsToTrip(registerParticipantsDTO);


        return ResponseEntity.ok().body(result.stream().map(participant -> new ParticipantListResponseDTO(
               participant.getId(),
               participant.getName(),
               participant.getEmail(),
               participant.getIsConfirmed()
        )));
    }

    @PostMapping("/{id}/activity")
    public ResponseEntity<Object> createActivity(@RequestBody CreateActivityDTO createActivityDTO, @PathVariable String id) {

        try {
            Trip trip = this.tripService.getTrip(UUID.fromString(id));

            var result = this.activityService.createActivity(createActivityDTO, trip);

            return ResponseEntity.status(201).body(result);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }
}
