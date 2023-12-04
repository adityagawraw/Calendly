package com.example.Calendly.repository;

import com.example.Calendly.model.ScheduledMeet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduledMeetRepository extends JpaRepository<ScheduledMeet, Long> {
}
