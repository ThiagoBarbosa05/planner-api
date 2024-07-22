package com.thiago.planner.activity;

import com.thiago.planner.activity.dto.CreateActivityDTO;
import com.thiago.planner.activity.dto.ListActivityResponseDTO;
import com.thiago.planner.exceptions.ActivitySameTimeException;
import com.thiago.planner.exceptions.TripDateIntervalsException;
import com.thiago.planner.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;

    @Autowired
    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public Activity createActivity(CreateActivityDTO createActivityDTO, Trip trip) {

        Optional<Activity> hasActivityInSameTime = this.activityRepository.findByOccursAt(LocalDateTime.parse(createActivityDTO.occursAt(), DateTimeFormatter.ISO_DATE_TIME));

        if(hasActivityInSameTime.isPresent()) {
            throw new ActivitySameTimeException("Já existe atividade nesse horário/dia.");
        }

        LocalDateTime occursAt = LocalDateTime.parse(createActivityDTO.occursAt(), DateTimeFormatter.ISO_DATE_TIME);
        LocalDateTime tripStartsAt = trip.getStartsAt();
        LocalDateTime tripEndsAt = trip.getEndsAt();

        if (occursAt.isBefore(tripStartsAt) || occursAt.isAfter(tripEndsAt)) {
            throw new TripDateIntervalsException("As atividades da viagem devem estar entre o início e o término da mesma.");
        }

        Activity activity = new Activity(createActivityDTO.title(), occursAt, trip);

        return this.activityRepository.save(activity);
    }

    public List<ListActivityResponseDTO> listAllActivitiesFromTrip(UUID tripId) {
        return this.activityRepository.
                findByTripId(tripId)
                .stream()
                .map(activity -> new ListActivityResponseDTO(activity.getId(), activity.getTitle(), activity.getOccursAt())).toList();
    }
}
