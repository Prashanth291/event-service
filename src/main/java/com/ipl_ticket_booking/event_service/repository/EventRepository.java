package com.ipl_ticket_booking.event_service.repository;

import com.ipl_ticket_booking.event_service.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}