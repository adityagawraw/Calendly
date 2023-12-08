package com.example.Calendly.model;

import java.time.LocalDate;

public class MeetDate {
    private boolean isAvailable;
    private LocalDate localDate;

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }
}
