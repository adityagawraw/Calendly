package com.example.Calendly.model;

import java.time.LocalTime;
import java.util.*;

public class SchedulingSetting {
    private int dateRange;
    private Map<String,List<Availability>> availabilityPerDay = new LinkedHashMap<>();
    private int maxPerDay;

    public SchedulingSetting() {
        for(Days day: Days.values()){
            List<Availability> availableHoursOnADay = new ArrayList<>();
            Availability availableHoursByDay = new Availability();

            availableHoursByDay.setDayOfWeek(String.valueOf(day));
            LocalTime startTime = LocalTime.of(9,0);
            LocalTime endTime = LocalTime.of(12,0);

            availableHoursByDay.setStartTime(startTime);
            availableHoursByDay.setEndTime(endTime);

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

    public Map<String, List<Availability>> getAvailabilityPerDay() {
        return availabilityPerDay;
    }

    public void setAvailabilityPerDay(Map<String, List<Availability>> availabilityPerDay) {
        this.availabilityPerDay = availabilityPerDay;
    }

    public void setAvailabilityListForADay(String day,List<Availability> availabilityOnADay){
        availabilityPerDay.put(day, availabilityOnADay);
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
