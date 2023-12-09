package com.example.Calendly.controller;

import com.example.Calendly.model.Event;
import com.example.Calendly.model.MeetDate;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
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
    public String showCalendar(Model model,
                               @RequestParam("eventId") long eventId,
                               @RequestParam(value = "selectedDate",defaultValue = "") LocalDate selectedDate,
                               @RequestParam(value = "date") LocalDate currentDate
                               ) {
        List<List<MeetDate>> daysInMonth = eventService.getDaysInMonth(currentDate, eventId);
        List<TimeSlot> timeSlots = eventService.findAvailableSlot(selectedDate, eventId);

        model.addAttribute("event", eventService.findEvent(eventId));
        model.addAttribute("daysInMonth", daysInMonth);
        model.addAttribute("eventId", eventId);
        model.addAttribute("selectedDate", selectedDate);
        model.addAttribute("date", currentDate);
        model.addAttribute("timeslots", timeSlots);
        model.addAttribute("month", currentDate.getMonth());
        model.addAttribute("selectedMonth", selectedDate.getMonth());
        model.addAttribute("selectedDayOfWeek", selectedDate.getDayOfWeek());
        model.addAttribute("selectedDayOfMonth", selectedDate.getDayOfMonth());

        return "select-timeslot";
    }

    @GetMapping("/nextMonth")
    public String nextMonth(@RequestParam("eventId") long eventId,
                            @RequestParam("selectedDate") LocalDate selectedDate,
                            @RequestParam(value = "date") LocalDate currentDate){
        return "redirect:/select-timeslot?selectedDate="+selectedDate+"&eventId="+eventId+"&date="
                +currentDate.plusMonths(1);
    }

    @GetMapping("/previousMonth")
    public String previousMonth(@RequestParam("eventId") long eventId,
                                @RequestParam("selectedDate") LocalDate selectedDate,
                                @RequestParam(value = "date") LocalDate currentDate){
        return "redirect:/select-timeslot?selectedDate="+selectedDate+"&eventId="+eventId+"&date="
                +currentDate.minusMonths(1);
    }

    @GetMapping("/date")
    public String handleDate(@RequestParam("eventId") long eventId,
                             @RequestParam("selectedDate") LocalDate selectedDate) {

        return "redirect:/select-timeslot?selectedDate="+selectedDate+"&eventId="+eventId;
    }

    @PostMapping("/create-meet")
    public String createMeet(@RequestParam("startHour") int startHour,
                             @RequestParam("startMinute") int startMinute,
                             @RequestParam("endHour") int endHour,
                             @RequestParam("endMinute") int endMinute,
                             @RequestParam("selectedDate") LocalDate selectedDate,
                             @RequestParam("eventId") long eventId){
        ScheduledMeet scheduledMeet = new ScheduledMeet();

        LocalTime startTime = LocalTime.of(startHour, startMinute);
        LocalTime endTime = LocalTime.of(endHour, endMinute);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, selectedDate.getYear());
        calendar.set(Calendar.MONTH, selectedDate.getMonth().getValue() - 1); // Month is 0-indexed in Calendar
        calendar.set(Calendar.DAY_OF_MONTH, selectedDate.getDayOfMonth());

        Event event = eventService.findEvent(eventId);
        scheduledMeet.setEvent(event);

        Date customDate = calendar.getTime();

        scheduledMeet.setStartTime(startTime);
        scheduledMeet.setEndTime(endTime);
        scheduledMeet.setHost(event.getHost());
        scheduledMeet.setDate(customDate);

        scheduledMeetService.saveScheduledMeet(scheduledMeet);

        return "redirect:/schedule-meet?meetId="+scheduledMeet.getId()+"&eventId="+eventId;
    }

    @GetMapping("/schedule-meet")
    public String scheduledMeet(Model model,
                                @RequestParam("meetId") long meetId,
                                @RequestParam("eventId") long eventId) {
        Event event = eventService.findEvent(eventId);
        String host = event.getHost().getName();
        ScheduledMeet meet = scheduledMeetService.findScheduledMeetById(meetId);

        model.addAttribute("event", event);
        model.addAttribute("host", host);
        model.addAttribute("meetId", meetId);
        model.addAttribute("scheduledMeet", scheduledMeetService.findScheduledMeetById(meetId));

        return "schedule-meeting";
    }
    @PostMapping("/save-schedule-meet")
    public String saveScheduledMeet(
                                      @RequestParam("inviteeName") String inviteeName,
                                      @RequestParam("inviteeEmail") String inviteeEmail,
                                      @RequestParam("meetId") long meetId,
                                      @RequestParam("description") String description,
                                      Model model) {

        ScheduledMeet scheduledMeet = scheduledMeetService.findScheduledMeetById(meetId);
        scheduledMeet.setInviteeName(inviteeName);
        scheduledMeet.setInviteeEmail(inviteeEmail);
        scheduledMeet.setDescription(description);

        scheduledMeetService.saveScheduledMeet(scheduledMeet);

        model.addAttribute("email",inviteeEmail);

        return "/scheduled-success";
    }



    @GetMapping("/scheduledMeets")
    public String findAllScheduledMeets(Model model) {
        List<ScheduledMeet> scheduledMeets = scheduledMeetService.findAllScheduledMeetsByHost();
        model.addAttribute("scheduledMeets", scheduledMeets);
        return "scheduled-meets";
    }


    @GetMapping("/deleteSchedule")
    public String deleteSchedule(@RequestParam("id") long eventId) {
        scheduledMeetService.deleteSchedule(eventId);
        return "redirect:/dashboard";
    }
}
