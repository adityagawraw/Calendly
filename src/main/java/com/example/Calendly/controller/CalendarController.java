package com.example.Calendly.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/calendar")
public class CalendarController {

    @GetMapping
    public String showCalendar(Model model) {
        List<List<LocalDate>> daysInMonth = getDaysInMonth(LocalDate.now());
        model.addAttribute("daysInMonth", daysInMonth);
        for(List<LocalDate> week : daysInMonth){
            for(LocalDate day : week){
                System.out.print(day+" ");
            }
            System.out.println();
        }
        return "calendar";
    }

    @GetMapping("/date")
    public String handleDate(@RequestParam("selectedDate") String selectedDate) {
        // Handle the date logic here
        System.out.println("Selected date: " + selectedDate);
        return "redirect:/calendar";
    }

    private List<List<LocalDate>> getDaysInMonth(LocalDate date) {
        List<List<LocalDate>> daysInMonth = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);
        int days = yearMonth.lengthOfMonth();

        // Start with the first day of the month
        LocalDate firstDayOfMonth = LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), 1);

        // Initialize the list for the current week
        List<LocalDate> currentWeek = new ArrayList<>();

        // Add empty cells for the days before the first day of the month
        for (int i = 1; i < firstDayOfMonth.getDayOfWeek().getValue(); i++) {
            currentWeek.add(null);
        }

        // Iterate through the days of the month
        for (int day = 1; day <= days; day++) {
            currentWeek.add(LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), day));

            // If we reach the end of the week, start a new week
            if (currentWeek.size() == DayOfWeek.values().length) {
                daysInMonth.add(currentWeek);
                currentWeek = new ArrayList<>();
            }
        }

        // Add empty cells for the days after the last day of the month
        while (currentWeek.size() < DayOfWeek.values().length) {
            currentWeek.add(null);
        }

        // Add the last week to the list
        daysInMonth.add(currentWeek);

        return daysInMonth;
    }
}

