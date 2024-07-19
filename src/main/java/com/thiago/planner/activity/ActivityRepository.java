package com.thiago.planner.activity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface ActivityRepository extends JpaRepository<Activity, UUID> {
    Optional<Activity> findByOccursAt(LocalDateTime occursAt);
}
