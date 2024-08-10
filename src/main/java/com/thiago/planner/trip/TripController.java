package com.thiago.planner.trip;


import com.thiago.planner.activity.Activity;
import com.thiago.planner.activity.ActivityService;
import com.thiago.planner.activity.dto.CreateActivityDTO;
import com.thiago.planner.activity.dto.ListActivityResponseDTO;
import com.thiago.planner.exceptions.ExceptionHandlerController;
import com.thiago.planner.link.Link;
import com.thiago.planner.link.LinkService;
import com.thiago.planner.link.dto.CreateLinkDTO;
import com.thiago.planner.link.dto.ListLinkResponseDTO;
import com.thiago.planner.mail.MailService;
import com.thiago.planner.participant.Participant;
import com.thiago.planner.participant.ParticipantService;
import com.thiago.planner.participant.dto.InviteParticipantsDTO;
import com.thiago.planner.participant.dto.ParticipantListResponseDTO;
import com.thiago.planner.participant.dto.RegisterParticipantsDTO;
import com.thiago.planner.trip.dto.CreateTripDTO;
import com.thiago.planner.trip.dto.UpdateTripDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/trip")
@CrossOrigin()
public class TripController {

    private final TripService tripService;
    private final ParticipantService participantService;
    private final ActivityService activityService;
    private final LinkService linkService;
    private final MailService mailService;


    @Autowired
    public TripController(
            TripService tripService,
            ParticipantService participantService,
            ActivityService activityService,
            LinkService linkService,
            MailService mailService

    ) {
        this.tripService = tripService;
        this.participantService = participantService;
        this.activityService = activityService;
        this.linkService = linkService;
        this.mailService = mailService;
    }

    @Tag(name = "Trip")
    @Operation(summary = "Criação de uma viagem", description = "Essa rota é responsável pela criação de uma viagem")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {
                    @Content(schema = @Schema(implementation = Trip.class))
            }),
            @ApiResponse(responseCode = "400", content = {
                    @Content(schema = @Schema(implementation = String.class))
            })
    })
    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody CreateTripDTO createTripDTO) {
        try {
            Trip result = this.tripService.createTrip(createTripDTO);


            RegisterParticipantsDTO registerParticipantsDTO = new RegisterParticipantsDTO(createTripDTO.emailsToInvite(), result);

            participantService.registerParticipantsToTrip(registerParticipantsDTO);

            this.mailService.sendMail(
                    createTripDTO.emailsToInvite().toArray(new String[]{""}),
                    "Confirmar viagem",
                    "Olá, você foi convidado por " + result.getOwnerName() + " a fazer parte de uma viagem para " + result.getDestination() + ", para confirmar a sua presença é só clicar no link http://localhost:3000/participant/confirm"
            );

            return ResponseEntity.status(201).body(result);
        }
        catch (Exception e) {

            return ExceptionHandlerController.handleExceptionResponse(e.getMessage());
        }
    }

    @Tag(name = "Trip")
    @Operation(summary = "Editar uma viagem", description = "Essa rota é responsável pela edição de uma viagem")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Trip.class))
            }),
            @ApiResponse(responseCode = "400", content = {
                    @Content(schema = @Schema(implementation = String.class))
            })
    })
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@Valid @RequestBody UpdateTripDTO updateTripDTO, @PathVariable String id) {
        try {
            Trip result = this.tripService.updateTrip(updateTripDTO, UUID.fromString(id));

            return ResponseEntity.ok().body(result);
        }
        catch (Exception e) {
            return ExceptionHandlerController.handleExceptionResponse(e.getMessage());
        }
    }

    @Tag(name = "Trip")
    @Operation(summary = "Listar participantes de uma viagem", description = "Essa rota é responsável pela listagem de todos os participantes de uma viagem.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(array = @ArraySchema(schema = @Schema(implementation = ParticipantListResponseDTO.class)))
            })
    })
    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ParticipantListResponseDTO>> getAllParticipants(@PathVariable String id) {
        List<ParticipantListResponseDTO> participants = this.participantService.getAllParticipantsFromTrip(UUID.fromString(id));

        return ResponseEntity.ok().body(participants);
    }

    @Tag(name = "Trip")
    @Operation(summary = "Convidar Participantes", description = "Essa rota é responsável por convidar participações para uma viagem.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(array = @ArraySchema(schema = @Schema(implementation = ParticipantListResponseDTO.class)))
            })
    })
    @PostMapping("/{id}/invite")
    public ResponseEntity<Object> inviteParticipants(@Valid @RequestBody InviteParticipantsDTO inviteParticipantsDTO, @PathVariable String id) {
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

    @Tag(name = "Trip")
    @Operation(summary = "Criação de atividade", description = "Essa rota é responsável pela criação de atividade para uma viagem")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {
                    @Content(schema = @Schema(implementation = Activity.class))
            }),
            @ApiResponse(responseCode = "400", content = {
                    @Content(schema = @Schema(implementation = String.class))
            })
    })
    @PostMapping("/{id}/activity")
    public ResponseEntity<Object> createActivity(@Valid @RequestBody CreateActivityDTO createActivityDTO, @PathVariable String id) {

        try {
            Trip trip = this.tripService.getTrip(UUID.fromString(id));

            var result = this.activityService.createActivity(createActivityDTO, trip);

            return ResponseEntity.status(201).body(result);
        }
        catch (Exception e) {
            return ExceptionHandlerController.handleExceptionResponse(e.getMessage());
        }
    }

    @Tag(name = "Trip")
    @Operation(summary = "Listar atividades", description = "Essa rota é responsável pela listagem de todas as atividades de uma viagem.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(array = @ArraySchema(schema = @Schema(implementation = ListActivityResponseDTO.class)))
            })
    })
    @GetMapping("/{id}/activity")
    public ResponseEntity<List<ListActivityResponseDTO>> listActivities(@PathVariable String id) {

        List<ListActivityResponseDTO> activities = this.activityService.listAllActivitiesFromTrip(UUID.fromString(id));

       return ResponseEntity.ok().body(activities);
    }

    @Tag(name = "Trip")
    @Operation(summary = "Criação de link", description = "Essa rota é responsável pela criação de link para uma viagem")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {
                    @Content(schema = @Schema(implementation = Link.class))
            })
    })
    @PostMapping("/{id}/link")
    public ResponseEntity<Object> createLink(@RequestBody CreateLinkDTO createLinkDTO, @PathVariable String id) {
            Trip trip = this.tripService.getTrip(UUID.fromString(id));

            var result = this.linkService.createLink(createLinkDTO, trip);

            return ResponseEntity.status(201).body(result);

    }

    @Tag(name = "Trip")
    @Operation(summary = "Listar links", description = "Essa rota é responsável pela listagem de todos os links de uma viagem.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(array = @ArraySchema(schema = @Schema(implementation = ListLinkResponseDTO.class)))
            })
    })
    @GetMapping("/{id}/link")
    public ResponseEntity<List<ListLinkResponseDTO>> listLinks(@PathVariable String id) {

        List<ListLinkResponseDTO> result = this.linkService.getAllLinksFromTrip(UUID.fromString(id));

        return ResponseEntity.ok().body(result);
    }
}
