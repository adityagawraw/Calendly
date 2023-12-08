package com.example.Calendly.service;

import com.example.Calendly.model.*;
import com.example.Calendly.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.*;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final AvailabilityRepository availabilityRepository;
    private final EventQuestionRepository eventQuestionRepository;
    private final UserRepository userRepository;
    private final AvailabilityService availabilityService;
    private final ScheduledMeetRepository scheduledMeetRepository;

    public EventService(EventRepository eventRepository,
                        AvailabilityRepository availabilityRepository,
                        EventQuestionRepository eventQuestionRepository,
                        UserRepository userRepository,
                        AvailabilityService availabilityService,
                        ScheduledMeetRepository scheduledMeetRepository) {
        this.eventRepository = eventRepository;
        this.availabilityRepository = availabilityRepository;
        this.eventQuestionRepository = eventQuestionRepository;
        this.userRepository = userRepository;
        this.availabilityService = availabilityService;
        this.scheduledMeetRepository = scheduledMeetRepository;


    }

    public SchedulingSetting getSchedulingSetting(long eventId) {
        Event event = findEvent(eventId);
        SchedulingSetting schedulingSetting = new SchedulingSetting();

        schedulingSetting.setDateRange(event.getDateRange());
        schedulingSetting.setMaxPerDay(event.getLimitPerDay());

        for (Availability availability : event.getAvailableHoursByDays()) {
            List<Availability> availabilities = schedulingSetting.getAvailabilityPerDay().get(availability.getDayOfWeek());
            Availability availability1 = availabilities.get(0);

            availability1.setStartTime(availability.getStartTime());
            availability1.setEndTime(availability.getEndTime());

            schedulingSetting.setAvailabilityListForADay(availability.getDayOfWeek(), availabilities);
        }

        return schedulingSetting;
    }

    public Set<String> getCheckedDays(long eventId) {
        Set<String> selectedDays = new HashSet<>();
        Event event = findEvent(eventId);

        for (Availability availability : event.getAvailableHoursByDays()) {
            selectedDays.add(availability.getDayOfWeek());
        }

        return selectedDays;
    }

    public List<Event> findAllEvents() {
        return eventRepository.findAll();
    }

    public List<Event> findEventsByHost() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail);
        List<Event> events = eventRepository.findByHost(user.getId());
        return events;
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

            availabilityRepository.deleteByEventId(eventId);

            for (String day : selectedDays) {
                List<Availability> availableHoursByDays = schedulingSetting.getAvailabilityPerDay().get(day);

                for (Availability availableHoursByDay : availableHoursByDays) {
                    availableHoursByDay.setEvent(event);
                    availableHoursByDay.setDayOfWeek(day);
                    availabilityRepository.save(availableHoursByDay);

                    event.addAvailableHoursByDay(availableHoursByDay);
                }
            }

            eventRepository.save(event);
        }
    }

    public Event findEvent(long eventId) {
        return eventRepository.findById(eventId).orElse(null);
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

    public List<TimeSlot> findAvailableSlot(LocalDate date, long eventId) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, date.getYear());
        calendar.set(Calendar.MONTH, date.getMonthValue()-1); // Month is 0-indexed in Calendar
        calendar.set(Calendar.DAY_OF_MONTH, date.getDayOfMonth());

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        Map<Integer, String> days = new HashMap<>();
        days.put(3, "SUNDAY");
        days.put(4, "MONDAY");
        days.put(5, "TUESDAY");
        days.put(6, "WEDNESDAY");
        days.put(7, "THURSDAY");
        days.put(1, "FRIDAY");
        days.put(2, "SATURDAY");

        // Convert Calendar to Date
        Date customDate = calendar.getTime();
        Event event = eventRepository.findById(eventId).get();
        long userId = event.getHost().getId();
        List<ScheduledMeet> scheduledMeets = scheduledMeetRepository.findMeetByHostAndDate(userId, customDate);
        List<ScheduledMeet> scheduledMeetsOfEvent = scheduledMeetRepository.findMeetByEventAndDate(eventId, customDate);

        Availability availability = availabilityService.findAvailabilityByDayOfWeekAndEvent(days.get(dayOfWeek), eventId);

        List<TimeSlot> timeslots = new ArrayList<>();

        if(availability==null || scheduledMeetsOfEvent.size()>=event.getLimitPerDay()){
            return timeslots;
        }

        int duration = findEvent(eventId).getDuration();
        LocalTime startTime = availability.getStartTime();
        LocalTime endTime = availability.getEndTime();
        LocalTime meetStartTime = startTime;
        LocalTime meetEndTime = meetStartTime.plusMinutes(duration);

        while (meetEndTime.isBefore(endTime) ||
                (meetEndTime.getHour()==endTime.getHour() && meetEndTime.getMinute()==endTime.getMinute())) {

            boolean flag = true;

            for (ScheduledMeet scheduledMeet: scheduledMeets) {
                if ((!meetStartTime.isBefore(scheduledMeet.getStartTime()) && meetStartTime.isBefore(scheduledMeet.getEndTime()) || meetStartTime.equals(scheduledMeet.getStartTime())) &&
                        (!meetEndTime.isBefore(scheduledMeet.getEndTime()) && meetEndTime.isBefore(scheduledMeet.getStartTime()) || meetEndTime.equals(scheduledMeet.getEndTime()))) {
                    flag = false;
                }
            }

            if (flag) {
                timeslots.add(new TimeSlot(meetStartTime, meetEndTime));
            }
            meetStartTime = meetEndTime;
            meetEndTime = meetEndTime.plusMinutes(duration);
        }

        return timeslots;
    }

    public List<List<LocalDate>> getDaysInMonth(LocalDate date) {
        List<List<LocalDate>> daysInMonth = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);
        int days = yearMonth.lengthOfMonth();

        // Start with the first day of the month
        LocalDate firstDayOfMonth = LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), 1);

        // Initialize the list for the current week
        List<LocalDate> currentWeek = new ArrayList<>();

        // Add empty cells for the days before the first day of the month
        for (int i = 1; i < firstDayOfMonth.getDayOfWeek().getValue(); i++) {
            currentWeek.add(null);
        }

        // Iterate through the days of the month
        for (int day = 1; day <= days; day++) {
            currentWeek.add(LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), day));

            // If we reach the end of the week, start a new week
            if (currentWeek.size() == DayOfWeek.values().length) {
                daysInMonth.add(currentWeek);
                currentWeek = new ArrayList<>();
            }
        }

        // Add empty cells for the days after the last day of the month
        while (currentWeek.size() < DayOfWeek.values().length) {
            currentWeek.add(null);
        }

        // Add the last week to the list
        daysInMonth.add(currentWeek);

        return daysInMonth;
    }
}
