package com.thiago.planner.activity;

import com.thiago.planner.activity.dto.CreateActivityDTO;
import com.thiago.planner.activity.dto.ListActivityResponseDTO;
import com.thiago.planner.exceptions.ActivitySameTimeException;
import com.thiago.planner.exceptions.TripDateIntervalsException;
import com.thiago.planner.trip.Trip;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ActivityServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    @InjectMocks
    private ActivityService activityService;

    @Test
    @DisplayName("Should create activity for a trip.")
    void shouldCreateActivity() {
        Trip trip = new Trip();
        trip.setStartsAt(LocalDateTime.now().plusDays(1));
        trip.setEndsAt(LocalDateTime.now().plusDays(4));
        CreateActivityDTO createActivityDTO = new CreateActivityDTO("Dinner", LocalDateTime.now().plusDays(2).plusHours(2).toString());
        Activity activity = new Activity(createActivityDTO.title(), LocalDateTime.parse(createActivityDTO.occursAt(), DateTimeFormatter.ISO_DATE_TIME), trip);

        when(activityRepository.findByOccursAt(activity.getOccursAt())).thenReturn(Optional.empty());
        when(activityRepository.save(any(Activity.class))).thenReturn(activity);

        Activity result = this.activityService.createActivity(createActivityDTO, trip);

        assertNotNull(result);
        assertEquals(result, activity);
        verify(activityRepository, times(1)).save(any(Activity.class));
    }

    @Test
    @DisplayName("should throw an exception if any activity occurs at the same time.")
    void shouldThrowExceptionIfActivityOccursSameTime() {
        Trip trip = new Trip();
        trip.setStartsAt(LocalDateTime.now().plusDays(1));
        trip.setEndsAt(LocalDateTime.now().plusDays(4));
        CreateActivityDTO createActivityDTO = new CreateActivityDTO("Dinner", LocalDateTime.now().minusDays(1).toString());
        Activity activity = new Activity(createActivityDTO.title(), LocalDateTime.parse(createActivityDTO.occursAt(), DateTimeFormatter.ISO_DATE_TIME), trip);

        when(activityRepository.findByOccursAt(activity.getOccursAt())).thenReturn(Optional.of(activity));

        assertThrows(ActivitySameTimeException.class, () -> {
            this.activityService.createActivity(createActivityDTO, trip);
        });

        verify(activityRepository, never()).save(any(Activity.class));
    }

    @Test
    @DisplayName("should throw an exception if an activity occurs outside trip dates.")
    void shouldThrowExceptionIfActivityOccursOutsideTripDate() {
        Trip trip = new Trip();
        trip.setStartsAt(LocalDateTime.now().plusDays(1));
        trip.setEndsAt(LocalDateTime.now().plusDays(4));
        CreateActivityDTO createActivityDTO = new CreateActivityDTO("Dinner", LocalDateTime.now().minusDays(1).toString());

        assertThrows(TripDateIntervalsException.class, () -> {
            this.activityService.createActivity(createActivityDTO, trip);
        });

        verify(activityRepository, never()).save(any(Activity.class));
    }

    @Test
    @DisplayName("should return activity list from trip.")
    void shouldReturnActivityListFromTrip() {
       List<Activity> activityList = List.of(
               new Activity(),
               new Activity()
       );

        UUID tripId = UUID.randomUUID();

        when(activityRepository.findByTripId(tripId)).thenReturn(activityList);

        List<ListActivityResponseDTO> result = this.activityService.listAllActivitiesFromTrip(tripId);

        assertNotNull(result);
        verify(activityRepository, times(1)).findByTripId(tripId);
    }
}