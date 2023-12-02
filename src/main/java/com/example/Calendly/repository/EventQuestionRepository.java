package com.example.Calendly.repository;

import com.example.Calendly.model.EventQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventQuestionRepository extends JpaRepository<EventQuestion, Long> {
}
