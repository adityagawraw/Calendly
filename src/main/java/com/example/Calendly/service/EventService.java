package com.example.Calendly.service;

import com.example.Calendly.model.Event;
import org.springframework.stereotype.Service;

public interface EventService {
    Event createEvent(String title, String description, int duration, String location,
                      String eventColor);
}
