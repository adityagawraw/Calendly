package com.example.Calendly.controller;

import com.example.Calendly.service.EventService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EventController {
    public final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/createEventForm")
    public  String getCreateEventPage(){
        return "create-event";
    }

    public String createEvent(
            Model model,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("duration") int duration,
            @RequestParam("location") String location,
            @RequestParam("eventColor") String eventColor) {
        eventService.createEvent(title, description, duration, location, eventColor);
        return "dashboard";
    }
}
