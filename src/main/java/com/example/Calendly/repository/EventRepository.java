package com.example.Calendly.repository;

import com.example.Calendly.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query(value = "select * from event where user_id = :userId", nativeQuery = true)
    List<Event> findByHost(long userId);
}
