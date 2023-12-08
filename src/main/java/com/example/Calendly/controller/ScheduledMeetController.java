package com.example.Calendly.controller;

import com.example.Calendly.model.Event;
import com.example.Calendly.model.ScheduledMeet;
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

    @GetMapping("select-timeslot")
    public String showCalendar(Model model) {
        List<List<LocalDate>> daysInMonth = getDaysInMonth(LocalDate.now());
        model.addAttribute("daysInMonth", daysInMonth);

        return "select-timeslot";
    }

    @GetMapping("/date")
    public String handleDate(@RequestParam("selectedDate") LocalDate selectedDate) {
        // Handle the date logic here
//        System.out.println("Selected date: " + selectedDate);
        return "redirect:/select-timeslot";
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
    private List<List<LocalDate>> getDaysInMonth(LocalDate date) {
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
