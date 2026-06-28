package com.ipl_ticket_booking.event_service.repository;

import com.ipl_ticket_booking.event_service.entity.Seat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    boolean existsByEventId(Long eventId);

    @EntityGraph(attributePaths = {
            "seatCategory",
            "event"
    })
    Page<Seat> findByEventId(
            Long eventId,
            Pageable pageable
    );

    long countByEventId(Long eventId);
}