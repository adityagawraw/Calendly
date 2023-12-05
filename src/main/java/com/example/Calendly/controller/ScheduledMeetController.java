package com.example.Calendly.controller;

import com.example.Calendly.model.Event;
import com.example.Calendly.model.ScheduledMeet;
import com.example.Calendly.repository.EventRepository;
import com.example.Calendly.service.ScheduledMeetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class ScheduledMeetController {
    private final ScheduledMeetService scheduledMeetService;
    private final EventRepository eventRepository;

    @Autowired
    public ScheduledMeetController(ScheduledMeetService scheduledMeetService, EventRepository eventRepository) {
        this.scheduledMeetService = scheduledMeetService;
        this.eventRepository = eventRepository;
    }

    @PostMapping("/schedule-meet")
    public String createScheduledMeet(
                                      @RequestParam("inviteeName") String inviteeName,
                                      @RequestParam("inviteeEmail") String inviteeEmail,
                                      @RequestParam("startHour") int startHour,
                                      @RequestParam("startMinute") int startMinute,
                                      @RequestParam("endHour") int endHour,
                                      @RequestParam("endMinute") int endMinute,
                                      @RequestParam("year") int year,
                                      @RequestParam("month") int month,
                                      @RequestParam("day") int day,
                                      @RequestParam("eventId") long eventId
                                      ) {

        ScheduledMeet scheduledMeet = new ScheduledMeet();
        scheduledMeet.setInviteeName(inviteeName);
        scheduledMeet.setInviteeEmail(inviteeEmail);

        LocalTime startTime = LocalTime.of(startHour, startMinute);
        LocalTime endTime = LocalTime.of(endHour, endMinute);

        // Create a Calendar instance and set year, month, and day
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1); // Month is 0-indexed in Calendar
        calendar.set(Calendar.DAY_OF_MONTH, day);

        // Convert Calendar to Date
        Date customDate = calendar.getTime();

        scheduledMeet.setStartTime(startTime);
        scheduledMeet.setEndTime(endTime);
        scheduledMeet.setDate(customDate);
        Event event = eventRepository.findById(eventId).orElse(null);
        scheduledMeet.setEvent(event);
        scheduledMeetService.saveScheduledMeet(scheduledMeet);

        return "/scheduled-success";
    }

    @GetMapping("/scheduled-meet/{eventId}")
    public String scheduledMeet(Model model, @PathVariable("eventId") long eventId) {
        model.addAttribute("eventId", eventId);
        return "schedule-meeting";
    }

    @GetMapping("/scheduledMeets")
    public String findAllScheduledMeets(Model model) {
        List<ScheduledMeet> scheduledMeets = scheduledMeetService.findAllScheduledMeets();
        model.addAttribute("scheduledMeets", scheduledMeets);
        return "scheduled-meets";
    }

}
