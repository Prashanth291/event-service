package com.ipl_ticket_booking.event_service.dto.response;

import com.ipl_ticket_booking.event_service.entity.EventStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class EventResponse {

    private Long id;

    private Long venueId;

    private String venueName;

    private String title;

    private String description;

    private LocalDateTime eventDateTime;

    private LocalDateTime bookingStart;

    private LocalDateTime bookingEnd;

    private EventStatus status;
}