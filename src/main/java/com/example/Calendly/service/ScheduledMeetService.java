package com.example.Calendly.service;

import com.example.Calendly.model.ScheduledMeet;
import org.springframework.ui.Model;

public interface ScheduledMeetService {
    void saveScheduledMeet(ScheduledMeet scheduledMeet, Model model);

}
