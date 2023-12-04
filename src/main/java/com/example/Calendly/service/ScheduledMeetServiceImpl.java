package com.example.Calendly.service;

import com.example.Calendly.model.ScheduledMeet;
import com.example.Calendly.repository.ScheduledMeetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

@Service
public class ScheduledMeetServiceImpl implements ScheduledMeetService {
    private final ScheduledMeetRepository scheduledMeetRepository;

    @Autowired
    public ScheduledMeetServiceImpl(ScheduledMeetRepository scheduledMeetRepository) {
        this.scheduledMeetRepository = scheduledMeetRepository;
    }

    @Override
    public void saveScheduledMeet(ScheduledMeet scheduledMeet) {
        scheduledMeetRepository.save(scheduledMeet);
    }
}
