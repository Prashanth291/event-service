package com.ipl_ticket_booking.event_service.repository;

import com.ipl_ticket_booking.event_service.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VenueRepository extends JpaRepository<Venue, Long> {

    Optional<Venue> findByName(String name);

    boolean existsByNameIgnoreCase(String name);

}
