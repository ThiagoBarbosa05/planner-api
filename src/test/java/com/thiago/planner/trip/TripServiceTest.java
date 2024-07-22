package com.thiago.planner.trip;

import com.thiago.planner.exceptions.TripDateIntervalsException;
import com.thiago.planner.trip.dto.CreateTripDTO;
import com.thiago.planner.trip.dto.UpdateTripDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TripServiceTest {

    @Mock
    private TripRepository tripRepository;

    @InjectMocks
    private TripService tripService;

    @Test
    @DisplayName("Should create a trip")
    void shouldCreateTrip() {
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

        when(tripRepository.save(any(Trip.class))).thenReturn(trip);

        Trip result = tripService.createTrip(createTripDTO);

        assertNotNull(result);
        assertEquals(result, trip);
    }

    @Test
    @DisplayName("Should not create a trip if starts date is before or equals than current date")
    void shouldNotCreateTripIfStartsDateIsBeforeOrEqualsThanCurrentDate() {
        CreateTripDTO tripDTO = new CreateTripDTO(
                "TEST user",
                "test@email.com",
                List.of("invite1@email.com", "invite2@email.com"),
                "statue of liberty, USA",
                false,
                LocalDateTime.now().minusDays(2).toString(),
                LocalDateTime.now().plusDays(5).toString()
        );


        assertThrows(TripDateIntervalsException.class,  () -> {
            this.tripService.createTrip(tripDTO);
        });

        verify(tripRepository, never()).save(any(Trip.class));
    }

    @Test
    @DisplayName("Should not create a trip if ends date is before than starts date")
    void shouldNotCreateTripIfEndsDateIsBeforeOrEqualsThanStartsDate() {
        CreateTripDTO tripWithBeforeDate = new CreateTripDTO(
                "TEST user",
                "test@email.com",
                List.of("invite1@email.com", "invite2@email.com"),
                "statue of liberty, USA",
                false,
                LocalDateTime.now().plusDays(2).toString(),
                LocalDateTime.now().toString()
        );


       assertThrows(TripDateIntervalsException.class,  () -> {
           this.tripService.createTrip(tripWithBeforeDate);
       });

       verify(tripRepository, never()).save(any(Trip.class));
    }


    @Test
    @DisplayName("Should update a trip")
    void shouldUpdateTrip() {
        UpdateTripDTO updateTripDTO = new UpdateTripDTO(
                "statue of liberty, USA",
                LocalDateTime.now().plusDays(3).toString(),
                LocalDateTime.now().plusDays(6).toString()
        );

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

        when(tripRepository.save(any(Trip.class))).thenReturn(trip);
        when(tripRepository.findTripById(trip.getId())).thenReturn(Optional.of(trip));


        Trip result = tripService.updateTrip(updateTripDTO, trip.getId());

        assertNotNull(result);
        assertEquals(result, trip);
    }

    @Test
    @DisplayName("Should return trip by id")
    void shouldReturnTripById() {
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

        when(tripRepository.findById(trip.getId())).thenReturn(Optional.of(trip));

        Trip result = this.tripService.getTrip(trip.getId());

        verify(tripRepository, times(1)).findById(trip.getId());
        assertNotNull(result);
        assertEquals(result, trip);
    }
}