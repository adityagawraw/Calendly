package com.example.Calendly.controller;

import com.example.Calendly.model.Event;
import com.example.Calendly.model.User;
import com.example.Calendly.service.EventService;
import com.example.Calendly.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class EventController {
    private final EventService eventService;
    private final UserService userService;
    @Autowired
    public EventController(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;
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
            @RequestParam("location") String location,
            @RequestParam("eventColor") String eventColor) {
        eventService.createEvent(title, description, duration, location, eventColor);

        return "create-event";
    }
}
