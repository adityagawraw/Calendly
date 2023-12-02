package com.example.Calendly.service;

import com.example.Calendly.model.Event;
import com.example.Calendly.repository.EventRepository;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventService{
    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Event createEvent(String title, String description, int duration, String location,
                             String eventColor) {
        Event event = new Event(title, description, duration, location, eventColor);
        Event savedEvent = eventRepository.save(event);
        return savedEvent;
    }

    public Event updateEvent(String title, String description, int duration, String location,
                             String eventColor) {

        return null;
    }

}
