package com.example.Calendly.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.List;
import java.util.Map;

//@Entity
//@Table(name = "entity")
public class Event {
    private long id;
    private String title;
    private final String host ="aditya";
    private String description;
    private int duration;
    private String location;
    private String eventColor;
    private int dateRange;
    private List<AvailableHoursByDay> availableHoursByDays;
    private int limitPerDay;
    private String inviteeName;
    private String inviteeEmail;
    private List<String> meetQuestions;
    private boolean emailConfirmation;
}
