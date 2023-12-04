package com.example.Calendly.controller;

import com.example.Calendly.model.ScheduledMeet;
import com.example.Calendly.service.ScheduledMeetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ScheduledMeetController {
    private final ScheduledMeetService scheduledMeetService;

    @Autowired
    public ScheduledMeetController(ScheduledMeetService scheduledMeetService) {
        this.scheduledMeetService = scheduledMeetService;
    }

    @GetMapping("/schedule-meet/{eventId}")
    public String createScheduledMeet(Model model
//                                      @RequestParam("inviteeName") String inviteeName,
//                                      @RequestParam("inviteeEmail") String inviteeEmail,
//                                      @RequestParam("startHour") int startHour,
//                                      @RequestParam("startMinute") int startMinute,
//                                      @RequestParam("endHour") int endHour,
//                                      @RequestParam("endMinute") int endMinute,
//                                      @RequestParam("year") int year,
//                                      @RequestParam("month") int month,
//                                      @RequestParam("day") int day,
//                                      @PathVariable("eventId") int eventId
                                      ) {

        ScheduledMeet newScheduledMeet = new ScheduledMeet();
        scheduledMeetService.saveScheduledMeet(newScheduledMeet, model);

        return "dashboard";
    }

}
