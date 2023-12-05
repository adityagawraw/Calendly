package com.example.Calendly.controller;

import com.example.Calendly.model.DaysCheckBox;
import com.example.Calendly.model.SchedulingSetting;
import com.example.Calendly.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EventController {
    public final EventService eventService;
    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
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

}
