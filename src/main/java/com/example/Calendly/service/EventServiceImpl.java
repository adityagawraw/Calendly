package com.example.Calendly.service;

import com.example.Calendly.model.Availability;
import com.example.Calendly.model.Event;
import com.example.Calendly.model.EventQuestion;
import com.example.Calendly.model.SchedulingSetting;
import com.example.Calendly.repository.AvailabilityRepository;
import com.example.Calendly.repository.EventQuestionRepository;
import com.example.Calendly.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService{
    private EventRepository eventRepository;
    private AvailabilityRepository availabilityRepository;
    private EventQuestionRepository eventQuestionRepository;

    public EventServiceImpl(EventRepository eventRepository,
                            AvailabilityRepository availabilityRepository,
                            EventQuestionRepository eventQuestionRepository) {
        this.eventRepository = eventRepository;
        this.availabilityRepository = availabilityRepository;
        this.eventQuestionRepository = eventQuestionRepository;
    }

    @Override
    public List<Event> findAllEvents() {
        return eventRepository.findAll();
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
                List<Availability> availableHoursByDays = schedulingSetting.getAvailabilityPerDay().get(day);

                for(Availability availableHoursByDay : availableHoursByDays){
                    availableHoursByDay.setEvent(event);
                    availabilityRepository.save(availableHoursByDay);
                    event.addAvailableHoursByDay(availableHoursByDay);
                }
            }

            eventRepository.save(event);
        }
    }

    public void saveBookingPageOptions(Long eventId, String eventLink, String inviteeQuestions){
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if(optionalEvent.isPresent()){
            Event event = optionalEvent.get();
            event.setEventLink(eventLink);
            String []meetQuestions = inviteeQuestions.split(",");

            for(String question: meetQuestions){
                EventQuestion eventQuestion = new EventQuestion();
                eventQuestion.setQuestion(question);
                eventQuestion.setEvent(event);
                eventQuestionRepository.save(eventQuestion);

                event.addMeetQuestions(eventQuestion);
            }
        }
    }
}
