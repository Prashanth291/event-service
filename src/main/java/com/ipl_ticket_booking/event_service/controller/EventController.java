package com.ipl_ticket_booking.event_service.controller;

import com.ipl_ticket_booking.event_service.common.dto.ApiResponse;
import com.ipl_ticket_booking.event_service.common.dto.PageResponse;
import com.ipl_ticket_booking.event_service.dto.request.CreateEventRequest;
import com.ipl_ticket_booking.event_service.dto.request.UpdateEventRequest;
import com.ipl_ticket_booking.event_service.dto.response.EventResponse;
import com.ipl_ticket_booking.event_service.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ApiResponse<EventResponse> createEvent(
            @Valid @RequestBody CreateEventRequest request) {

        return ApiResponse.<EventResponse>builder()
                .success(true)
                .message("Event created successfully.")
                .data(eventService.createEvent(request))
                .build();
    }

    @GetMapping
    public ApiResponse<PageResponse<EventResponse>> getAllEvents(
            Pageable pageable) {

        return ApiResponse.<PageResponse<EventResponse>>builder()
                .success(true)
                .message("Events fetched successfully.")
                .data(eventService.getAllEvents(pageable))
                .build();
    }

    @GetMapping("/{eventId}")
    public ApiResponse<EventResponse> getEventById(
            @PathVariable Long eventId) {

        return ApiResponse.<EventResponse>builder()
                .success(true)
                .message("Event fetched successfully.")
                .data(eventService.getEventById(eventId))
                .build();
    }

    @PutMapping("/{eventId}")
    public ApiResponse<EventResponse> updateEvent(
            @PathVariable Long eventId,
            @Valid @RequestBody UpdateEventRequest request) {

        return ApiResponse.<EventResponse>builder()
                .success(true)
                .message("Event updated successfully.")
                .data(eventService.updateEvent(eventId, request))
                .build();
    }

    @DeleteMapping("/{eventId}")
    public ApiResponse<Void> deleteEvent(
            @PathVariable Long eventId) {

        eventService.deleteEvent(eventId);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Event deleted successfully.")
                .build();
    }

    @PostMapping("/{eventId}/publish")
    public ApiResponse<EventResponse> publishEvent(
            @PathVariable Long eventId) {

        EventResponse response = eventService.publishEvent(eventId);

        return ApiResponse.<EventResponse>builder()
                .success(true)
                .message("Event published successfully.")
                .data(response)
                .build();
    }
}