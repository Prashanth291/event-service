package com.ipl_ticket_booking.event_service.repository;

import com.ipl_ticket_booking.event_service.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.Optional;


public interface EventRepository extends JpaRepository<Event, Long> {

    @Override
    @EntityGraph(attributePaths = "venue")
    Page<Event> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "venue")
    Optional<Event> findById(Long id);

}