package com.example.Calendly.repository;

import com.example.Calendly.model.AvailableHoursByDay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvailableHoursByDayRepository extends JpaRepository<AvailableHoursByDay, Long> {
}
