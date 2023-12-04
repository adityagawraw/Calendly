package com.example.Calendly.model;

import java.util.ArrayList;
import java.util.List;

public class DaysCheckBox {
    private List<String> selectedDays = new ArrayList<>();

    public List<String> getSelectedDays() {
        return selectedDays;
    }

    public void setSelectedDays(List<String> selectedDays) {
        this.selectedDays = selectedDays;
    }
}
