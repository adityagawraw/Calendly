package com.example.Calendly.repository;

import com.example.Calendly.model.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {

    @Modifying
    @Query(value = "delete from availabilities where event_id = :eventId" , nativeQuery = true)
    void deleteByEventId(long eventId);
}
