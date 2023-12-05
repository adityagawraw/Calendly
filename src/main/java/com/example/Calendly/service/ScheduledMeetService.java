package com.example.Calendly.service;

import com.example.Calendly.model.ScheduledMeet;
import com.example.Calendly.repository.ScheduledMeetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ScheduledMeetService{
    private final ScheduledMeetRepository scheduledMeetRepository;

    @Autowired
    public ScheduledMeetService(ScheduledMeetRepository scheduledMeetRepository) {
        this.scheduledMeetRepository = scheduledMeetRepository;
    }
    public void saveScheduledMeet(ScheduledMeet scheduledMeet) {
        scheduledMeetRepository.save(scheduledMeet);
    }

    public List<ScheduledMeet> findAllScheduledMeets() {
        List<ScheduledMeet> scheduledMeets = scheduledMeetRepository.findAll();
        return scheduledMeets;
    }
}
