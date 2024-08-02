package com.thiago.planner.trip;

import com.thiago.planner.exceptions.ResourceNotFoundException;
import com.thiago.planner.exceptions.TripDateIntervalsException;
import com.thiago.planner.trip.dto.CreateTripDTO;
import com.thiago.planner.trip.dto.UpdateTripDTO;
import com.thiago.planner.utils.ValidDatesForTrip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class TripService {

    private final TripRepository tripRepository;

    @Autowired
    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public Trip createTrip(CreateTripDTO createTripDTO) {
        LocalDateTime startsAt = LocalDateTime.parse(createTripDTO.startsAt(), DateTimeFormatter.ISO_DATE_TIME);
        LocalDateTime endsAt = LocalDateTime.parse(createTripDTO.endsAt(), DateTimeFormatter.ISO_DATE_TIME);


        if (ValidDatesForTrip.isCurrentDateEqualOrAfterStartsDate(startsAt)) {
            throw new TripDateIntervalsException("A data de início da viagem não pode ser anterior ou na mesma data atual.");
        }

        if (ValidDatesForTrip.isEndsDateBeforeStartsDate(startsAt ,endsAt)) {
            throw new TripDateIntervalsException("A data final da viagem não pode ser anterior a data de início.");
        }

        Trip trip = new Trip(createTripDTO);
        return this.tripRepository.save(trip);
    }

    public Trip updateTrip(UpdateTripDTO updateTripDTO, UUID tripId) {
        LocalDateTime startsAt = LocalDateTime.parse(updateTripDTO.startsAt(), DateTimeFormatter.ISO_DATE_TIME);
        LocalDateTime endsAt = LocalDateTime.parse(updateTripDTO.endsAt(), DateTimeFormatter.ISO_DATE_TIME);

        if (ValidDatesForTrip.isCurrentDateEqualOrAfterStartsDate(startsAt)) {
            throw new TripDateIntervalsException("A data de início da viagem não pode ser anterior ou na mesma data atual.");
        }

        if (ValidDatesForTrip.isEndsDateBeforeStartsDate(startsAt ,endsAt)) {
            throw new TripDateIntervalsException("A data final da viagem não pode ser anterior a data de início.");
        }

        Trip trip = this.tripRepository.findTripById(tripId).orElseThrow(() -> new ResourceNotFoundException("Viagem não encontrada"));

        trip.setDestination(updateTripDTO.destination());
        trip.setStartsAt(startsAt);
        trip.setEndsAt(endsAt);

        return this.tripRepository.save(trip);
    }

    public Trip getTrip(UUID tripId) {
        return this.tripRepository.findById(tripId).orElseThrow(() -> new ResourceNotFoundException("Viagem não encontrada."));
    }

}
