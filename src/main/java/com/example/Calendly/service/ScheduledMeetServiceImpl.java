package com.example.Calendly.service;

import com.example.Calendly.model.ScheduledMeet;
import com.example.Calendly.repository.ScheduledMeetRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

@Service
public class ScheduledMeetServiceImpl {
    private final ScheduledMeetRepository scheduledMeetRepository;

    @Autowired
    public ScheduledMeetServiceImpl(ScheduledMeetRepository scheduledMeetRepository) {
        this.scheduledMeetRepository = scheduledMeetRepository;
    }

    public void saveScheduledMeet(ScheduledMeet scheduledMeet, Model model) {

        scheduledMeet.setInviteeName("anand");
        scheduledMeet.setInviteeEmail("anand@gmail.com");
        scheduledMeet.setInviteeName("Anand");
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(11, 0);
        int year = 2023;
        int month = 12; // December
        int day = 31;

// Create a Calendar instance and set year, month, and day
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1); // Month is 0-indexed in Calendar
        calendar.set(Calendar.DAY_OF_MONTH, day);

// Convert Calendar to Date
        Date customDate = calendar.getTime();
        scheduledMeet.setStartTime(startTime);
        scheduledMeet.setEndTime(endTime);
        scheduledMeet.setDate(customDate);
        scheduledMeetRepository.save(scheduledMeet);
    }
}
