package com.ipl_ticket_booking.event_service.controller;

import com.ipl_ticket_booking.event_service.common.dto.ApiResponse;
import com.ipl_ticket_booking.event_service.common.dto.PageResponse;
import com.ipl_ticket_booking.event_service.dto.response.SeatResponse;
import com.ipl_ticket_booking.event_service.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/events/{eventId}/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    @GetMapping
    public ApiResponse<PageResponse<SeatResponse>> getSeatsByEvent(
            @PathVariable Long eventId,
            @PageableDefault(size = 20, sort = "rowLabel") Pageable pageable) {

        PageResponse<SeatResponse> response =
                seatService.getSeatsByEvent(eventId, pageable);

        return ApiResponse.<PageResponse<SeatResponse>>builder()
                .success(true)
                .message("Seats fetched successfully.")
                .data(response)
                .build();
    }
}