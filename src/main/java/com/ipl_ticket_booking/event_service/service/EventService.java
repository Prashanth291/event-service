package com.ipl_ticket_booking.event_service.service;

import com.ipl_ticket_booking.event_service.common.dto.PageResponse;
import com.ipl_ticket_booking.event_service.dto.request.CreateEventRequest;
import com.ipl_ticket_booking.event_service.dto.request.UpdateEventRequest;
import com.ipl_ticket_booking.event_service.dto.response.EventResponse;
import org.springframework.data.domain.Pageable;

public interface EventService {

    EventResponse createEvent(CreateEventRequest request);

    PageResponse<EventResponse> getAllEvents(Pageable pageable);

    EventResponse getEventById(Long eventId);

    EventResponse updateEvent(Long eventId, UpdateEventRequest request);

    void deleteEvent(Long eventId);

}