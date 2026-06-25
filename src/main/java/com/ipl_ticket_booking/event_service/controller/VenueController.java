package com.ipl_ticket_booking.event_service.controller;

import com.ipl_ticket_booking.event_service.common.dto.ApiResponse;
import com.ipl_ticket_booking.event_service.dto.request.CreateVenueRequest;
import com.ipl_ticket_booking.event_service.dto.response.VenueResponse;
import com.ipl_ticket_booking.event_service.service.VenueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

}