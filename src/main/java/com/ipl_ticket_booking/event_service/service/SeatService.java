package com.ipl_ticket_booking.event_service.service;

import com.ipl_ticket_booking.event_service.common.dto.PageResponse;
import com.ipl_ticket_booking.event_service.dto.response.SeatResponse;
import org.springframework.data.domain.Pageable;

public interface SeatService {

    PageResponse<SeatResponse> getSeatsByEvent(
            Long eventId,
            Pageable pageable
    );

}