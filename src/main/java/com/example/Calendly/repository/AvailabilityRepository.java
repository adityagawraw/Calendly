package com.example.Calendly.repository;

import com.example.Calendly.model.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {

    @Modifying
    @Query(value = "delete from availabilities where event_id = :eventId" , nativeQuery = true)
    void deleteByEventId(long eventId);

    @Query(value = "select * from availabilities where day_of_week = :dayOfWeek and event_id = :eventId", nativeQuery = true)
    Availability findAvailabilityByDayAndEvent(@Param("dayOfWeek") String dayOfWeek, @Param("eventId") long eventId);
}
