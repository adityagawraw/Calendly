package com.example.Calendly.service;

import com.example.Calendly.model.Event;
import com.example.Calendly.model.SchedulingSetting;
import org.springframework.stereotype.Service;

import java.util.List;

public interface EventService {
    Event createEvent(String title, String description, int duration, String location,
                      String eventColor);
    public void saveScheduleSettings(Long eventId, List<String> selectedDays, SchedulingSetting schedulingSetting);
    public void saveBookingPageOptions(Long eventId, String eventLink, String inviteeQuestions);
}
