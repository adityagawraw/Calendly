package com.example.Calendly.controller;

import com.example.Calendly.model.Event;
import com.example.Calendly.model.ScheduledMeet;
import com.example.Calendly.model.TimeSlot;
import com.example.Calendly.service.EventService;
import com.example.Calendly.service.ScheduledMeetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class ScheduledMeetController {
    private final ScheduledMeetService scheduledMeetService;
    private final EventService eventService;

    @Autowired
    public ScheduledMeetController(ScheduledMeetService scheduledMeetService, EventService eventService) {
        this.scheduledMeetService = scheduledMeetService;
        this.eventService = eventService;
    }

    @GetMapping("/select-timeslot")
    public String showCalendar(Model model,@RequestParam("eventId") long eventId,
                               @RequestParam(value = "selectedDate",defaultValue = "") LocalDate selectedDate
                               ) {
        List<List<LocalDate>> daysInMonth = eventService.getDaysInMonth(LocalDate.now());
        model.addAttribute("event", eventService.findEvent(eventId));
        model.addAttribute("daysInMonth", daysInMonth);
        model.addAttribute("eventId", eventId);
        model.addAttribute("selectedDate", selectedDate);



        List<TimeSlot> timeSlots = eventService.findAvailableSlot(selectedDate, eventId);
        model.addAttribute("timeslots", timeSlots);

        return "select-timeslot";
    }

    @GetMapping("/date")
    public String handleDate(@RequestParam("eventId") long eventId,
                             @RequestParam("selectedDate") LocalDate selectedDate) {

        return "redirect:/select-timeslot?selectedDate="+selectedDate+"&eventId="+eventId;
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
                                      @RequestParam("eventId") long eventId,
                                      @RequestParam("description") String description,
                                      Model model) {

        ScheduledMeet scheduledMeet = new ScheduledMeet();
        scheduledMeet.setInviteeName(inviteeName);
        scheduledMeet.setInviteeEmail(inviteeEmail);
        scheduledMeet.setDescription(description);

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
        Event event = eventService.findEvent(eventId);
        scheduledMeet.setEvent(event);
        scheduledMeet.setHost(event.getHost());
        scheduledMeetService.saveScheduledMeet(scheduledMeet);

        model.addAttribute("email",inviteeEmail);

        return "/scheduled-success";
    }

    @GetMapping("/scheduled-meet/{eventId}")
    public String scheduledMeet(Model model, @PathVariable("eventId") long eventId) {
        Event event = eventService.findEvent(eventId);
        String host = event.getHost().getName();

        model.addAttribute("event", event);
        model.addAttribute("host", host);
        return "schedule-meeting";
    }

    @GetMapping("/scheduledMeets")
    public String findAllScheduledMeets(Model model) {
        List<ScheduledMeet> scheduledMeets = scheduledMeetService.findAllScheduledMeetsByHost();
        model.addAttribute("scheduledMeets", scheduledMeets);
        return "scheduled-meets";
    }

}
