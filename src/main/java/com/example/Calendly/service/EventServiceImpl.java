package com.example.Calendly.service;

import com.example.Calendly.model.Event;
import com.example.Calendly.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl implements EventService{
    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public List<Event> findAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Event createEvent(String title, String description, int duration, String location) {
        Event event = new Event(title, description, duration, location);
        Event savedEvent = eventRepository.save(event);
        return savedEvent;
    }

    @Override
    public Event findEvent(long eventId) {
        Event event = eventRepository.findById(eventId).orElse(null);
        return event;
    }

    public Event updateEvent(String title, String description, int duration, String location,
                             String eventColor) {

        return null;
    }

}
