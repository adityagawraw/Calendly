package com.example.Calendly.controller;

import com.example.Calendly.model.Event;
import com.example.Calendly.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class EventController {
    public final EventService eventService;
    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        List<Event> events = eventService.findAllEvents();
        model.addAttribute("events", events);

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
