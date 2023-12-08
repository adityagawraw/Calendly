package com.example.Calendly.service;

import com.example.Calendly.model.Availability;
import com.example.Calendly.repository.AvailabilityRepository;
import org.springframework.stereotype.Service;

@Service
public class AvailabilityService {
    public final AvailabilityRepository availabilityRepository;

    public AvailabilityService(AvailabilityRepository availabilityRepository) {
        this.availabilityRepository = availabilityRepository;
    }

    public Availability findAvailabilityByDayOfWeekAndEvent(String dayOfWeek, long eventId) {
        Availability availability = availabilityRepository.findAvailabilityByDayAndEvent(dayOfWeek, eventId);

        return availability;
    }
}
