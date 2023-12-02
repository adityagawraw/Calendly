package com.example.Calendly.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EventController {


    @GetMapping("/create-event")
    public  String getCreateEventPage(){
        return "create-event";
    }
}
