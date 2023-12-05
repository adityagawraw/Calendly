package com.example.Calendly.service;

import com.example.Calendly.model.ScheduledMeet;

import java.util.List;

public interface ScheduledMeetService {
    void saveScheduledMeet(ScheduledMeet scheduledMeet);

    List<ScheduledMeet> findAllScheduledMeets();

}
