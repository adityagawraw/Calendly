package com.example.Calendly.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "host")
    private String host ="aditya";

    @Column(name = "description")
    private String description;

    @Column(name = "duration")
    private int duration;

    @Column(name = "location")
    private String location;

    @Column(name = "event_color")
    private String eventColor;

    @Column(name = "date_range")
    private int dateRange;

    @OneToMany(mappedBy = "event")
    private List<AvailableHoursByDay> availableHoursByDays;

    @Column(name = "limit_per_day")
    private int limitPerDay;

//    @Column(name = "invitee_name")
//    private String inviteeName;
//
//    @Column(name = "invitee_email")
//    private String inviteeEmail;

    @OneToMany(mappedBy = "event")
    private List<EventQuestion> meetQuestions;

    public Event(String title, String description, int duration, String location, String eventColor) {
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.location = location;
        this.eventColor = eventColor;
    }


//    @Column(name = "email_confirmation")
//    private boolean emailConfirmation;
}
