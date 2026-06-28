package com.ipl_ticket_booking.event_service.repository;

import com.ipl_ticket_booking.event_service.entity.SeatCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeatCategoryRepository extends JpaRepository<SeatCategory, Long> {

    boolean existsByEventIdAndNameIgnoreCase(
            Long eventId,
            String name
    );

    boolean existsByEventIdAndNameIgnoreCaseAndIdNot(
            Long eventId,
            String name,
            Long id
    );

    Page<SeatCategory> findByEventId(
            Long eventId,
            Pageable pageable
    );

    Optional<SeatCategory> findByIdAndEventId(
            Long seatCategoryId,
            Long eventId
    );


}