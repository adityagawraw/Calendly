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

    @OneToMany(mappedBy = "event")
    private List<EventQuestion> meetQuestions;

    public Event(String title, String description, int duration, String location, String eventColor) {
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.location = location;
        this.eventColor = eventColor;
    }

    public void addavailableHoursByDay(AvailableHoursByDay availableHoursByDay){
        availableHoursByDays.add(availableHoursByDay);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEventColor() {
        return eventColor;
    }

    public void setEventColor(String eventColor) {
        this.eventColor = eventColor;
    }

    public int getDateRange() {
        return dateRange;
    }

    public void setDateRange(int dateRange) {
        this.dateRange = dateRange;
    }

    public List<AvailableHoursByDay> getAvailableHoursByDays() {
        return availableHoursByDays;
    }

    public void setAvailableHoursByDays(List<AvailableHoursByDay> availableHoursByDays) {
        this.availableHoursByDays = availableHoursByDays;
    }

    public int getLimitPerDay() {
        return limitPerDay;
    }

    public void setLimitPerDay(int limitPerDay) {
        this.limitPerDay = limitPerDay;
    }

    public List<EventQuestion> getMeetQuestions() {
        return meetQuestions;
    }

    public void setMeetQuestions(List<EventQuestion> meetQuestions) {
        this.meetQuestions = meetQuestions;
    }
}
