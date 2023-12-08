package com.example.Calendly.repository;

import com.example.Calendly.model.Event;
import com.example.Calendly.model.ScheduledMeet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface ScheduledMeetRepository extends JpaRepository<ScheduledMeet, Long> {
    @Query(value = "select * from scheduled_meets where user_id = :userId", nativeQuery = true)
    List<ScheduledMeet> findMeetByLoggedInUser(long userId);
    @Query(value = "select * from scheduled_meets where DATE(date) = DATE(:date)", nativeQuery = true)
    List<ScheduledMeet> findScheduledMeetByDate(Date date);

    @Query(value = "select * from scheduled_meets where user_id = :userId and DATE(date) = DATE(:date)", nativeQuery = true)
    List<ScheduledMeet> findMeetByHostAndDate(long userId, Date date);
}
