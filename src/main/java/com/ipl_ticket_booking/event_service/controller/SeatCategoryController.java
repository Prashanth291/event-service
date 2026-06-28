package com.ipl_ticket_booking.event_service.controller;

import com.ipl_ticket_booking.event_service.common.dto.ApiResponse;
import com.ipl_ticket_booking.event_service.common.dto.PageResponse;
import com.ipl_ticket_booking.event_service.dto.request.CreateSeatCategoryRequest;
import com.ipl_ticket_booking.event_service.dto.request.UpdateSeatCategoryRequest;
import com.ipl_ticket_booking.event_service.dto.response.SeatCategoryResponse;
import com.ipl_ticket_booking.event_service.service.SeatCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/events/{eventId}/seat-categories")
@RequiredArgsConstructor
public class SeatCategoryController {

    private final SeatCategoryService seatCategoryService;

    @PostMapping
    public ApiResponse<SeatCategoryResponse> createSeatCategory(
            @PathVariable Long eventId,
            @Valid @RequestBody CreateSeatCategoryRequest request) {

        return ApiResponse.<SeatCategoryResponse>builder()
                .success(true)
                .message("Seat category created successfully.")
                .data(seatCategoryService.createSeatCategory(eventId, request))
                .build();
    }

    @GetMapping
    public ApiResponse<PageResponse<SeatCategoryResponse>> getSeatCategories(
            @PathVariable Long eventId,
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {

        return ApiResponse.<PageResponse<SeatCategoryResponse>>builder()
                .success(true)
                .message("Seat categories fetched successfully.")
                .data(seatCategoryService.getSeatCategoriesByEvent(eventId, pageable))
                .build();
    }

    @GetMapping("/{seatCategoryId}")
    public ApiResponse<SeatCategoryResponse> getSeatCategoryById(
            @PathVariable Long eventId,
            @PathVariable Long seatCategoryId) {

        return ApiResponse.<SeatCategoryResponse>builder()
                .success(true)
                .message("Seat category fetched successfully.")
                .data(seatCategoryService.getSeatCategoryById(eventId, seatCategoryId))
                .build();
    }

    @PutMapping("/{seatCategoryId}")
    public ApiResponse<SeatCategoryResponse> updateSeatCategory(
            @PathVariable Long eventId,
            @PathVariable Long seatCategoryId,
            @Valid @RequestBody UpdateSeatCategoryRequest request) {

        return ApiResponse.<SeatCategoryResponse>builder()
                .success(true)
                .message("Seat category updated successfully.")
                .data(seatCategoryService.updateSeatCategory(
                        eventId,
                        seatCategoryId,
                        request))
                .build();
    }

    @DeleteMapping("/{seatCategoryId}")
    public ApiResponse<Void> deleteSeatCategory(
            @PathVariable Long eventId,
            @PathVariable Long seatCategoryId) {

        seatCategoryService.deleteSeatCategory(
                eventId,
                seatCategoryId);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Seat category deleted successfully.")
                .build();
    }
}