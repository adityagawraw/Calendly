package com.example.Calendly.controller;

import com.example.Calendly.model.*;
import com.example.Calendly.service.AvailabilityService;
import com.example.Calendly.service.EventService;
import com.example.Calendly.service.ScheduledMeetService;
import com.example.Calendly.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalTime;
import java.util.*;
import java.time.Duration;



@Controller
public class EventController {
    private final EventService eventService;
    private final UserService userService;
    private final ScheduledMeetService scheduledMeetService;
    private final AvailabilityService availabilityService;

    @Autowired
    public EventController(
            EventService eventService,
            UserService userService,
            ScheduledMeetService scheduledMeetService,
            AvailabilityService availabilityService
    ) {
        this.eventService = eventService;
        this.userService = userService;
        this.scheduledMeetService = scheduledMeetService;
        this.availabilityService = availabilityService;
    }

    @GetMapping("/")
    public String home(Model model) {
        return "homepage";
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        List<Event> events = eventService.findEventsByHost();
        model.addAttribute("events", events);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(authentication.getName());

        List<ScheduledMeet> scheduledMeets = scheduledMeetService.findAllScheduledMeetsByHost();
        model.addAttribute("scheduledMeets", scheduledMeets);
        model.addAttribute("user", user);

        return "dashboard";
    }


    @GetMapping("/create-event")
    public String getCreateEventPage(@RequestParam("eventId") long eventId, Model model) {
        model.addAttribute("eventId", eventId);
        model.addAttribute("event", eventService.findEvent(eventId));


        return "create-event";
    }

    @GetMapping("/event-details")
    public String getEventDetailsPage(@RequestParam("eventId") long eventId, Model model) {
        model.addAttribute("eventId", eventId);
        Event event = eventService.findEvent(eventId);
        model.addAttribute("title", event.getTitle());
        model.addAttribute("description", event.getDescription());
        model.addAttribute("duration", event.getDuration());
        model.addAttribute("location", event.getLocation());

        return "event-details";
    }
    
    @GetMapping("/update-event-details")
    public String updateEventDetails(
            Model model,
            @RequestParam("eventId") long eventId,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("duration") int duration,
            @RequestParam("location") String location
    ) {
        eventService.updateEventDetails(eventId, title, description, duration, location);

        return "redirect:/create-event?eventId=" + eventId;
    }

    @GetMapping("/new-event")
    public String getNewEventPage() {
        return "new-event";
    }

    @GetMapping("/save-event-details")
    public String saveEventDetails(
            Model model,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("duration") int duration,
            @RequestParam("location") String location) {
        Event event = eventService.createEvent(title, description, duration, location);

        return "redirect:/create-event?eventId=" + event.getId();
    }

    @GetMapping("/scheduling-settings")
    public String getSchedulingSettingsPage(@RequestParam("eventId") long eventID,
                                            Model model) {
        SchedulingSetting schedulingSetting = eventService.getSchedulingSetting(eventID);
        model.addAttribute("schedulingSettings", schedulingSetting);
        model.addAttribute("eventId", eventID);

        DaysCheckBox daysCheckBox = new DaysCheckBox();
        Set<String> selectedDays = eventService.getCheckedDays(eventID);

        for (String day : selectedDays) {
            daysCheckBox.addSelectedDays(day);
        }

        model.addAttribute("daysCheckBox", daysCheckBox);

        return "scheduling-settings";
    }

    @GetMapping("/save-scheduling-settings")
    public String saveSchedulingSettings(@ModelAttribute("schedulingSettings") SchedulingSetting schedulingSetting,
                                         @ModelAttribute("daysCheckBox") DaysCheckBox daysCheckBox,
                                         @RequestParam("eventId") long eventId,
                                         Model model) {
        Event event = eventService.findEvent(eventId);

        for (Map.Entry<String, List<Availability>> entry : schedulingSetting.getAvailabilityPerDay().entrySet()) {
            Availability availability = entry.getValue().get(0);
            Duration duration = Duration.between(availability.getStartTime(), availability.getEndTime());

            if (availability.getStartTime().isAfter(availability.getEndTime()) ||
                    availability.getStartTime().equals(availability.getEndTime())) {
                return "redirect:/scheduling-settings?eventId=" + eventId;
            }

            if (duration.toMinutes() < event.getDuration()){
                return "redirect:/scheduling-settings?eventId=" + eventId;
            }


        }

        eventService.saveScheduleSettings(eventId, daysCheckBox.getSelectedDays(), schedulingSetting);

        return "redirect:/create-event?eventId=" + eventId;
    }

    @GetMapping("/booking-page-options")
    public String getBookingPageOptions(@RequestParam("eventId") long eventID, Model model) {
        model.addAttribute("eventId", eventID);
        model.addAttribute("eventLink", "");
        model.addAttribute("inviteeQuestions", "");

        return "booking-page-options";
    }

    @GetMapping("/save-booking-page-options")
    public String saveBookingPageOptions(@ModelAttribute("eventId") long eventId,
                                         @RequestParam("eventLink") String eventLink,
                                         @RequestParam("inviteeQuestions") String inviteeQuestions,
                                         Model model) {
        eventService.saveBookingPageOptions(eventId, eventLink, inviteeQuestions);

        return "redirect:/create-event?eventId=" + eventId;
    }

    @GetMapping("/events")
    public String findEvent(Model model, @RequestParam("eventId") long eventId) {
        Event event = eventService.findEvent(eventId);
        model.addAttribute("event", event);

        return "event";
    }

    @GetMapping("/events/delete/{eventId}")
    public String deleteEvent(Model model, @PathVariable("eventId") long eventId) {
        eventService.deleteEvent(eventId);

        return "redirect:/dashboard";
    }

    // TimeSlot work in project
    @GetMapping("/timeslot")
    public String findAvailableSlot(
            Model model,
            @RequestParam("year") int year,
            @RequestParam("month") int month,
            @RequestParam("day") int day,
            @RequestParam("eventId") long eventId

    ) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month); // Month is 0-indexed in Calendar
        calendar.set(Calendar.DAY_OF_MONTH, day);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        Map<Integer, String> days = new HashMap<>();
        days.put(1, "SUNDAY");
        days.put(2, "MONDAY");
        days.put(3, "TUESDAY");
        days.put(4, "WEDNESDAY");
        days.put(5, "THURSDAY");
        days.put(6, "FRIDAY");
        days.put(7, "SATURDAY");

        // Convert Calendar to Date
        Date customDate = calendar.getTime();
        List<ScheduledMeet> scheduledMeets = scheduledMeetService.findAvailableTimeSlots(customDate);
        Availability availability = availabilityService.findAvailabilityByDayOfWeekAndEvent(days.get(dayOfWeek), eventId);

        List<TimeSlot> timeslots = new ArrayList<>();
        int duration = eventService.findEvent(eventId).getDuration();

        LocalTime startTime = availability.getStartTime();
        LocalTime endTime = availability.getEndTime();
        LocalTime meetStartTime = startTime;
        LocalTime meetEndTime = meetStartTime.plusMinutes(duration);

        while (meetEndTime.isBefore(endTime)) {
            timeslots.add(new TimeSlot(meetStartTime, meetEndTime));
            meetStartTime = meetEndTime;
            meetEndTime = meetEndTime.plusMinutes(duration);
        }

        System.out.println(timeslots);
        model.addAttribute("scheduledMeets", scheduledMeets);
        model.addAttribute("availability", availability);
        model.addAttribute("timeSlots", timeslots);

        return "trying";
    }
}
