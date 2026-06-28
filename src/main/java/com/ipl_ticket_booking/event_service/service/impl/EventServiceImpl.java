package com.ipl_ticket_booking.event_service.service.impl;

import com.ipl_ticket_booking.event_service.common.dto.PageResponse;
import com.ipl_ticket_booking.event_service.common.exception.BadRequestException;
import com.ipl_ticket_booking.event_service.common.exception.ResourceNotFoundException;
import com.ipl_ticket_booking.event_service.common.util.PageUtils;
import com.ipl_ticket_booking.event_service.dto.request.CreateEventRequest;
import com.ipl_ticket_booking.event_service.dto.request.UpdateEventRequest;
import com.ipl_ticket_booking.event_service.dto.response.EventResponse;
import com.ipl_ticket_booking.event_service.entity.Event;
import com.ipl_ticket_booking.event_service.entity.EventStatus;
import com.ipl_ticket_booking.event_service.entity.Venue;
import com.ipl_ticket_booking.event_service.mapper.EventMapper;
import com.ipl_ticket_booking.event_service.repository.EventRepository;
import com.ipl_ticket_booking.event_service.repository.SeatCategoryRepository;
import com.ipl_ticket_booking.event_service.repository.SeatRepository;
import com.ipl_ticket_booking.event_service.service.EventService;
import com.ipl_ticket_booking.event_service.service.VenueService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final VenueService venueService;
    private final EventMapper eventMapper;
    private final SeatCategoryRepository seatCategoryRepository;
    private final SeatRepository seatRepository;

    @Override
    public EventResponse createEvent(CreateEventRequest request) {

        Venue venue = venueService.findVenueEntityById(request.getVenueId());

        Event event = eventMapper.toEntity(request, venue);

        validateEvent(event);

        Event savedEvent = eventRepository.save(event);

        return eventMapper.toResponse(savedEvent);
    }

    @Override
    public PageResponse<EventResponse> getAllEvents(Pageable pageable) {

        Page<EventResponse> page = eventRepository
                .findAll(pageable)
                .map(eventMapper::toResponse);

        return PageUtils.toPageResponse(page);
    }

    @Override
    public EventResponse getEventById(Long eventId) {

        return eventMapper.toResponse(findEventById(eventId));
    }

    @Override
    public EventResponse updateEvent(Long eventId,
                                     UpdateEventRequest request) {

        Event event = findEventById(eventId);

        Venue venue = venueService.findVenueEntityById(request.getVenueId());

        eventMapper.updateEntity(event, request, venue);

        validateEvent(event);

        Event updatedEvent = eventRepository.save(event);

        return eventMapper.toResponse(updatedEvent);
    }

    @Override
    public void deleteEvent(Long eventId) {

        Event event = findEventById(eventId);

        eventRepository.delete(event);
    }

    @Override
    public Event findEventEntityById(Long eventId) {
        return findEventById(eventId);
    }

    private Event findEventById(Long eventId) {

        return eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Event not found with id: " + eventId
                        ));
    }

    private void validateEvent(Event event) {

        if (!event.getBookingStart().isBefore(event.getBookingEnd())) {

            throw new BadRequestException(
                    "Booking start must be before booking end."
            );
        }

        if (!event.getBookingEnd().isBefore(event.getEventDateTime())) {

            throw new BadRequestException(
                    "Booking end must be before event date."
            );
        }
    }

    @Override
    @Transactional
    public EventResponse publishEvent(Long eventId) {

        Event event = findEventEntityById(eventId);

        validatePublish(event);

        event.setStatus(EventStatus.PUBLISHED);

        return eventMapper.toResponse(event);
    }

    private void validatePublish(Event event) {

        if (event.getStatus() != EventStatus.DRAFT) {
            throw new BadRequestException(
                    "Only draft events can be published."
            );
        }

        Long eventId = event.getId();

        if (!seatCategoryRepository.existsByEventId(eventId)) {
            throw new BadRequestException(
                    "Configure seat categories before publishing."
            );
        }

        if (!seatRepository.existsByEventId(eventId)) {
            throw new BadRequestException(
                    "Generate seats before publishing."
            );
        }

        long generatedSeats = seatRepository.countByEventId(eventId);

        if (generatedSeats > event.getVenue().getMaxCapacity()) {
            throw new BadRequestException(
                    "Generated seats exceed venue capacity."
            );
        }
    }
}