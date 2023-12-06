package com.example.Calendly.service;

import com.example.Calendly.model.ScheduledMeet;
import com.example.Calendly.model.User;
import com.example.Calendly.repository.ScheduledMeetRepository;
import com.example.Calendly.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ScheduledMeetService{
    private final ScheduledMeetRepository scheduledMeetRepository;
    private final UserRepository userRepository;

    @Autowired
    public ScheduledMeetService(ScheduledMeetRepository scheduledMeetRepository, UserRepository userRepository) {
        this.scheduledMeetRepository = scheduledMeetRepository;
        this.userRepository = userRepository;
    }
    public void saveScheduledMeet(ScheduledMeet scheduledMeet) {
        scheduledMeetRepository.save(scheduledMeet);
    }

    public List<ScheduledMeet> findAllScheduledMeets() {
        List<ScheduledMeet> scheduledMeets = scheduledMeetRepository.findAll();
        return scheduledMeets;
    }

    public List<ScheduledMeet> findAllScheduledMeetsByHost() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail);
        List<ScheduledMeet> scheduledMeets = scheduledMeetRepository.findMeetByLoggedInUser(user.getId());
        return scheduledMeets;
    }
}
