package com.ipl_ticket_booking.event_service.controller;

import com.ipl_ticket_booking.event_service.common.dto.ApiResponse;
import com.ipl_ticket_booking.event_service.common.dto.PageResponse;
import com.ipl_ticket_booking.event_service.dto.request.CreateVenueRequest;
import com.ipl_ticket_booking.event_service.dto.response.VenueResponse;
import com.ipl_ticket_booking.event_service.service.VenueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/v1/venues")
@RequiredArgsConstructor
public class VenueController {

    private final VenueService venueService;

    @PostMapping
    public ApiResponse<VenueResponse> createVenue(
            @Valid @RequestBody CreateVenueRequest request) {

        VenueResponse response = venueService.createVenue(request);

        return ApiResponse.<VenueResponse>builder()
                .success(true)
                .message("Venue created successfully.")
                .data(response)
                .build();
    }
    @GetMapping
    public ApiResponse<PageResponse<VenueResponse>> getAllVenues(Pageable pageable) {

        return ApiResponse.<PageResponse<VenueResponse>>builder()
                .success(true)
                .message("Venues fetched successfully.")
                .data(venueService.getAllVenues(pageable))
                .build();
    }

    @GetMapping("/{venueId}")
    public ApiResponse<VenueResponse> getVenueById(
            @PathVariable Long venueId) {

        return ApiResponse.<VenueResponse>builder()
                .success(true)
                .message("Venue fetched successfully.")
                .data(venueService.getVenueById(venueId))
                .build();
    }

    @PutMapping("/{venueId}")
    public ApiResponse<VenueResponse> updateVenue(
            @PathVariable Long venueId,
            @Valid @RequestBody CreateVenueRequest request) {

        VenueResponse response =
                venueService.updateVenue(venueId, request);

        return ApiResponse.<VenueResponse>builder()
                .success(true)
                .message("Venue updated successfully.")
                .data(response)
                .build();
    }

    @DeleteMapping("/{venueId}")
    public ApiResponse<Void> deleteVenue(
            @PathVariable Long venueId) {

        venueService.deleteVenue(venueId);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Venue deleted successfully.")
                .build();
    }
}