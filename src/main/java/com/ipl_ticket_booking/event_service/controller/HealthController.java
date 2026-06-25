package com.ipl_ticket_booking.event_service.controller;

import com.ipl_ticket_booking.event_service.common.dto.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/api/v1/health")
    public ApiResponse<String> health() {

        return ApiResponse.<String>builder()
                .success(true)
                .message("Event Service is running")
                .data("UP")
                .build();
    }
}