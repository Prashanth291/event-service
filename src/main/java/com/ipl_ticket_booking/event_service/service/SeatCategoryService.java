package com.ipl_ticket_booking.event_service.service;

import com.ipl_ticket_booking.event_service.common.dto.PageResponse;
import com.ipl_ticket_booking.event_service.dto.request.CreateSeatCategoryRequest;
import com.ipl_ticket_booking.event_service.dto.request.UpdateSeatCategoryRequest;
import com.ipl_ticket_booking.event_service.dto.response.SeatCategoryResponse;
import com.ipl_ticket_booking.event_service.entity.SeatCategory;
import org.springframework.data.domain.Pageable;

public interface SeatCategoryService {

    SeatCategoryResponse createSeatCategory(
            Long eventId,
            CreateSeatCategoryRequest request
    );

    PageResponse<SeatCategoryResponse> getSeatCategoriesByEvent(
            Long eventId,
            Pageable pageable
    );

    SeatCategoryResponse getSeatCategoryById(
            Long eventId,
            Long seatCategoryId
    );

    SeatCategoryResponse updateSeatCategory(
            Long eventId,
            Long seatCategoryId,
            UpdateSeatCategoryRequest request
    );

    void deleteSeatCategory(
            Long eventId,
            Long seatCategoryId
    );

    SeatCategory findSeatCategoryEntityById(
            Long eventId,
            Long seatCategoryId
    );
}