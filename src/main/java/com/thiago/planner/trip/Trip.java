package com.thiago.planner.trip;

import com.thiago.planner.trip.dto.CreateTripDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name = "trips")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "owner_name", nullable = false)
    private String ownerName;

    @Column(name = "owner_email", nullable = false)
    private String ownerEmail;

    @Column(nullable = false)
    private String destination;

    @Column(name = "is_confirmed",nullable = false)
    private Boolean isConfirmed;

    @Column(name = "starts_at", nullable = false)
    private LocalDateTime startsAt;

    @Column(name = "ends_at", nullable = false)
    private LocalDateTime endsAt;

    public Trip(CreateTripDTO createTripDTO) {
        this.ownerName = createTripDTO.ownerName();
        this.ownerEmail = createTripDTO.ownerEmail();
        this.destination = createTripDTO.destination();
        this.isConfirmed = createTripDTO.isConfirmed();
        this.startsAt = LocalDateTime.parse(createTripDTO.startsAt(), DateTimeFormatter.ISO_DATE_TIME);
        this.endsAt = LocalDateTime.parse(createTripDTO.endsAt(), DateTimeFormatter.ISO_DATE_TIME);
    }
}

