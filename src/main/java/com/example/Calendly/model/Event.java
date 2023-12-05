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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User host;

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
    private List<Availability> availableHoursByDays;

    @Column(name = "limit_per_day")
    private int limitPerDay;

    @OneToMany(mappedBy = "event")
    private List<EventQuestion> meetQuestions;

    @OneToMany(mappedBy = "event")
    private List<ScheduledMeet> scheduledMeets;

    @Column(name = "event_link")
    private  String eventLink;

    public Event(String title, String description, int duration, String location) {
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.location = location;
    }


    public void addAvailableHoursByDay(Availability availableHoursByDay){
        availableHoursByDays.add(availableHoursByDay);
    }
    public void addMeetQuestions(EventQuestion eventQuestion){
        meetQuestions.add(eventQuestion);
    }

    public String getEventLink() {
        return eventLink;
    }

    public void setEventLink(String eventLink) {
        this.eventLink = eventLink;
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

    public User getHost() {
        return host;
    }

    public void setHost(User host) {
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

    public List<Availability> getAvailableHoursByDays() {
        return availableHoursByDays;
    }

    public void setAvailableHoursByDays(List<Availability> availableHoursByDays) {
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
