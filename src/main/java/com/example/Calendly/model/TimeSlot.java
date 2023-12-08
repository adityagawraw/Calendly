package com.example.Calendly.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class TimeSlot {
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;

    public TimeSlot(LocalTime startTime, LocalTime endTime) {
        this.startHour = startTime.getHour();
        this.startMinute = startTime.getMinute();
        this.endHour = endTime.getHour();
        this.endMinute = endTime.getMinute();
    }
}
