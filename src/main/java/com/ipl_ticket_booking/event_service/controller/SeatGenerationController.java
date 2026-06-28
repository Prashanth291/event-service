package com.ipl_ticket_booking.event_service.controller;

import com.ipl_ticket_booking.event_service.common.dto.ApiResponse;
import com.ipl_ticket_booking.event_service.service.SeatGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class SeatGenerationController {

    private final SeatGenerationService seatGenerationService;

    @PostMapping("/{eventId}/generate-seats")
    public ApiResponse<Integer> generateSeats(
            @PathVariable Long eventId) {

        int generatedSeats = seatGenerationService.generateSeats(eventId);

        return ApiResponse.<Integer>builder()
                .success(true)
                .message(generatedSeats + " seats generated successfully.")
                .data(generatedSeats)
                .build();
    }

}