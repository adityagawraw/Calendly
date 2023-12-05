package com.example.Calendly.service;

import com.example.Calendly.model.Event;
import org.springframework.stereotype.Service;

import java.util.List;

public interface EventService {

    List<Event> findAllEvents();
    Event createEvent(String title, String description, int duration, String location);

    Event findEvent(long eventId);
}
