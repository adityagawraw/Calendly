package com.example.Calendly.service;

import com.example.Calendly.model.AvailableHoursByDay;
import com.example.Calendly.model.Event;
import com.example.Calendly.model.SchedulingSetting;
import com.example.Calendly.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService{
    private EventRepository eventRepository;

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

    @Override
    public void saveScheduleSettings(Long eventId, List<String> selectedDays, SchedulingSetting schedulingSetting) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if(optionalEvent.isPresent()){
            Event event = optionalEvent.get();

            event.setDateRange(schedulingSetting.getDateRange());
            event.setLimitPerDay(schedulingSetting.getMaxPerDay());

            for(String day : selectedDays){
                List<AvailableHoursByDay> availableHoursByDays = schedulingSetting.getAvailabilityPerDay().get(day);
                for(AvailableHoursByDay availableHoursByDay : availableHoursByDays){
                    System.out.println(availableHoursByDay);
                }
                event.setAvailableHoursByDays(availableHoursByDays);
            }
            eventRepository.save(event);
        }
    }

    public Event updateEvent(String title, String description, int duration, String location,
                             String eventColor) {

        return null;
    }

}
