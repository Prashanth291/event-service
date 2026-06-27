package com.ipl_ticket_booking.event_service.mapper;

import com.ipl_ticket_booking.event_service.dto.request.CreateEventRequest;
import com.ipl_ticket_booking.event_service.dto.request.UpdateEventRequest;
import com.ipl_ticket_booking.event_service.dto.response.EventResponse;
import com.ipl_ticket_booking.event_service.entity.Event;
import com.ipl_ticket_booking.event_service.entity.EventStatus;
import com.ipl_ticket_booking.event_service.entity.Venue;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    public Event toEntity(CreateEventRequest request, Venue venue) {

        return Event.builder()
                .venue(venue)
                .title(request.getTitle())
                .description(request.getDescription())
                .eventDateTime(request.getEventDateTime())
                .bookingStart(request.getBookingStart())
                .bookingEnd(request.getBookingEnd())
                .status(EventStatus.DRAFT)
                .build();
    }

    public void updateEntity(Event event,
                             UpdateEventRequest request,
                             Venue venue) {

        event.setVenue(venue);
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setEventDateTime(request.getEventDateTime());
        event.setBookingStart(request.getBookingStart());
        event.setBookingEnd(request.getBookingEnd());
    }

    public EventResponse toResponse(Event event) {

        return EventResponse.builder()
                .id(event.getId())
                .venueId(event.getVenue().getId())
                .venueName(event.getVenue().getName())
                .title(event.getTitle())
                .description(event.getDescription())
                .eventDateTime(event.getEventDateTime())
                .bookingStart(event.getBookingStart())
                .bookingEnd(event.getBookingEnd())
                .status(event.getStatus())
                .build();
    }

}