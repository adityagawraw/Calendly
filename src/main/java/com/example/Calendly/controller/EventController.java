package com.example.Calendly.controller;

import com.example.Calendly.model.*;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class EventController {
    private final EventService eventService;
    private final UserService userService;
    private final ScheduledMeetService scheduledMeetService;
    @Autowired
    public EventController(EventService eventService, UserService userService, ScheduledMeetService scheduledMeetService) {
        this.eventService = eventService;
        this.userService = userService;
        this.scheduledMeetService = scheduledMeetService;
    }

    @GetMapping("/")
    public String home(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user=userService.findUserByEmail(authentication.getName());
        model.addAttribute("user",user);
        return "homepage";
    }
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        List<Event> events = eventService.findAllEvents();
        model.addAttribute("events", events);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user=userService.findUserByEmail(authentication.getName());

        List<ScheduledMeet> scheduledMeets = scheduledMeetService.findAllScheduledMeets();
        model.addAttribute("scheduledMeets", scheduledMeets);
        model.addAttribute("user",user);

        return "dashboard";

    }

    @GetMapping("/create-event")
    public  String getCreateEventPage() {
        return "create-event";
    }

    @GetMapping("/event-details")
    public  String getEventDetailsPage() {
        return "event-details";
    }

    @GetMapping("/save-event-details")
    public String saveEventDetails(
            Model model,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("duration") int duration,
            @RequestParam("location") String location
            ) {
        eventService.createEvent(title, description, duration, location);

        return "create-event";
    }

    @GetMapping("/scheduling-settings")
    public String getSchedulingSettingsPage(@RequestParam("eventId") long eventID,
                                            @ModelAttribute("schedulingSettings") SchedulingSetting schedulingSetting,
                                            Model model){
        model.addAttribute("schedulingSettings", new SchedulingSetting());
        model.addAttribute("eventId", eventID);
        model.addAttribute("daysCheckBox", new DaysCheckBox());

        return "scheduling-settings";
    }

    @GetMapping("/save-scheduling-settings")
    public String saveSchedulingSettings(@ModelAttribute("schedulingSettings") SchedulingSetting schedulingSetting,
                                         @ModelAttribute("daysCheckBox") DaysCheckBox daysCheckBox,
                                         @RequestParam("eventId") long eventId,
                                         Model model) {
        eventService.saveScheduleSettings(eventId, daysCheckBox.getSelectedDays(), schedulingSetting);

        return "redirect:/create-event";
    }
    @GetMapping("/booking-page-options")
    public String getBookingPageOptions(@RequestParam("eventId") long eventID, Model model){
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

        return "redirect:/create-event";
    }

    @GetMapping("/events")
    public String findEvent(Model model, @RequestParam("eventId") long eventId) {
        Event event = eventService.findEvent(eventId);
        model.addAttribute("event", event);

        return "event";
    }

    @GetMapping("/events/delete")
    public String deleteEvent(Model model, @RequestParam("eventId") long eventId) {
        eventService.deleteEvent(eventId);

        return "redirect:/dashboard";
    }
}
