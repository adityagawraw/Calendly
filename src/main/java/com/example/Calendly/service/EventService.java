package com.example.Calendly.service;

import com.example.Calendly.model.*;
import com.example.Calendly.repository.AvailabilityRepository;
import com.example.Calendly.repository.EventQuestionRepository;
import com.example.Calendly.repository.EventRepository;
import com.example.Calendly.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final AvailabilityRepository availabilityRepository;
    private final EventQuestionRepository eventQuestionRepository;
    private final UserRepository userRepository;

    public EventService(EventRepository eventRepository,
                        AvailabilityRepository availabilityRepository,
                        EventQuestionRepository eventQuestionRepository,
                        UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.availabilityRepository = availabilityRepository;
        this.eventQuestionRepository = eventQuestionRepository;
        this.userRepository = userRepository;
    }


    public List<Event> findAllEvents() {
        return eventRepository.findAll();
    }

    public List<Event> findEventsByHost() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail);
        List<Event> events = eventRepository.findByHost(user.getId());
        return  events;
    }

    public Event createEvent(String title, String description, int duration, String location) {
        Event event = new Event(title, description, duration, location);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail);
        event.setHost(user);
        Event savedEvent = eventRepository.save(event);
        return savedEvent;
    }

    public void saveScheduleSettings(Long eventId, List<String> selectedDays, SchedulingSetting schedulingSetting) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();

            event.setDateRange(schedulingSetting.getDateRange());
            event.setLimitPerDay(schedulingSetting.getMaxPerDay());

            List<Availability> availabilities = event.getAvailableHoursByDays();

            for (Availability availability : availabilities) {
                availabilityRepository.delete(availability);
            }

            for (String day : selectedDays) {
                List<Availability> availableHoursByDays = schedulingSetting.getAvailabilityPerDay().get(day);

                for (Availability availableHoursByDay : availableHoursByDays) {
                    availableHoursByDay.setEvent(event);
                    availableHoursByDay.setDay(day);
                    availabilityRepository.save(availableHoursByDay);

                    event.addAvailableHoursByDay(availableHoursByDay);
                }
            }

            eventRepository.save(event);
        }
    }


    public Event findEvent(long eventId) {
        Event event = eventRepository.findById(eventId).orElse(null);

        return event;
    }

    public void deleteEvent(long eventId) {
        eventRepository.deleteById(eventId);
    }


    public void updateEventDetails(long eventId, String title, String description, int duration, String location) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);

        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
            event.setTitle(title);
            event.setDescription(description);
            event.setDuration(duration);
            event.setLocation(location);

            eventRepository.save(event);
        }
    }

    public void saveBookingPageOptions(Long eventId, String eventLink, String inviteeQuestions) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
            event.setEventLink(eventLink);
            String[] meetQuestions = inviteeQuestions.split(",");

            List<EventQuestion> eventQuestions = event.getMeetQuestions();
            for (EventQuestion eventQuestion : eventQuestions) {
                eventQuestionRepository.delete(eventQuestion);
            }

            for (String question : meetQuestions) {
                EventQuestion eventQuestion = new EventQuestion();
                eventQuestion.setQuestion(question);
                eventQuestion.setEvent(event);
                eventQuestionRepository.save(eventQuestion);

                event.addMeetQuestions(eventQuestion);
            }
        }
    }
}
