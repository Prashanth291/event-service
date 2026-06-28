package com.ipl_ticket_booking.event_service.service.impl;

import com.ipl_ticket_booking.event_service.common.exception.BadRequestException;
import com.ipl_ticket_booking.event_service.entity.Event;
import com.ipl_ticket_booking.event_service.entity.Seat;
import com.ipl_ticket_booking.event_service.entity.SeatCategory;
import com.ipl_ticket_booking.event_service.repository.SeatCategoryRepository;
import com.ipl_ticket_booking.event_service.repository.SeatRepository;
import com.ipl_ticket_booking.event_service.service.EventService;
import com.ipl_ticket_booking.event_service.service.SeatGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SeatGenerationServiceImpl implements SeatGenerationService {

    private final EventService eventService;
    private final SeatCategoryRepository seatCategoryRepository;
    private final SeatRepository seatRepository;

    @Override
    public int generateSeats(Long eventId) {

        Event event = eventService.findEventEntityById(eventId);

        validateSeatGeneration(eventId);

        List<SeatCategory> seatCategories =
                seatCategoryRepository.findAllByEventId(eventId);

        List<Seat> generatedSeats = new ArrayList<>();

        for (SeatCategory category : seatCategories) {

            generatedSeats.addAll(
                    generateSeatsForCategory(event, category)
            );
        }

        validateVenueCapacity(event, generatedSeats.size());

        seatRepository.saveAll(generatedSeats);

        return generatedSeats.size();
    }

    /**
     * Generates seats for one category.
     */
    private List<Seat> generateSeatsForCategory(
            Event event,
            SeatCategory category) {

        List<Seat> seats = new ArrayList<>();

        char startRow = category.getStartRow();
        char endRow = category.getEndRow();

        for (char row = startRow; row <= endRow; row++) {

            for (int seatNumber = 1;
                 seatNumber <= category.getSeatsPerRow();
                 seatNumber++) {

                Seat seat = Seat.builder()
                        .event(event)
                        .seatCategory(category)
                        .rowLabel(row)
                        .seatNumber(seatNumber)
                        .seatLabel(buildSeatLabel(
                                category.getName(),
                                row,
                                seatNumber
                        ))
                        .build();

                seats.add(seat);
            }
        }

        return seats;
    }

    /**
     * Validates whether seats can be generated for the event.
     */
    private void validateSeatGeneration(Long eventId) {

        if (seatRepository.existsByEventId(eventId)) {
            throw new BadRequestException(
                    "Seats have already been generated for this event."
            );
        }

        if (seatCategoryRepository.findAllByEventId(eventId).isEmpty()) {
            throw new BadRequestException(
                    "No seat categories configured for this event."
            );
        }
    }

    /**
     * Ensures generated seats do not exceed venue capacity.
     */
    private void validateVenueCapacity(
            Event event,
            int generatedSeatCount) {

        int venueCapacity = event.getVenue().getMaxCapacity();

        if (generatedSeatCount > venueCapacity) {
            throw new BadRequestException(
                    String.format(
                            "Generated seats (%d) exceed venue capacity (%d).",
                            generatedSeatCount,
                            venueCapacity
                    )
            );
        }
    }

    /**
     * Builds labels like:
     * VIP-A-001
     * PREMIUM-C-020
     */
    private String buildSeatLabel(
            String categoryName,
            char row,
            int seatNumber) {

        return String.format(
                "%s-%c-%03d",
                categoryName.toUpperCase(),
                row,
                seatNumber
        );
    }
}