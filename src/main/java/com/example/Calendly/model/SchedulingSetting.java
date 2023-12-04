package com.example.Calendly.model;

import java.util.*;

 enum Days {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY
}

public class SchedulingSetting {
    private int dateRange;
    private Map<String,List<AvailableHoursByDay>> availabilityPerDay = new LinkedHashMap<>();
    private int maxPerDay;

    public SchedulingSetting() {
        for(Days day: Days.values()){
            List<AvailableHoursByDay> availableHoursOnADay = new ArrayList<>();
            AvailableHoursByDay availableHoursByDay = new AvailableHoursByDay();

            availableHoursByDay.setDay(String.valueOf(day));
            availableHoursByDay.setStartTime(9);
            availableHoursByDay.setEndTime(14);

            availableHoursOnADay.add(availableHoursByDay);
            availabilityPerDay.put(String.valueOf(day),availableHoursOnADay);
        }
    }

    public int getDateRange() {
        return dateRange;
    }

    public void setDateRange(int dateRange) {
        this.dateRange = dateRange;
    }



    public int getMaxPerDay() {
        return maxPerDay;
    }

    public void setMaxPerDay(int maxPerDay) {
        this.maxPerDay = maxPerDay;
    }

    public Map<String, List<AvailableHoursByDay>> getAvailabilityPerDay() {
        return availabilityPerDay;
    }

    public void setAvailabilityPerDay(Map<String, List<AvailableHoursByDay>> availabilityPerDay) {
        this.availabilityPerDay = availabilityPerDay;
    }

    @Override
    public String toString() {
        return "SchedulingSetting{" +
                "dateRange=" + dateRange +
                ", availabilityPerDay=" + availabilityPerDay +
                ", maxPerDay=" + maxPerDay +
                '}';
    }
}