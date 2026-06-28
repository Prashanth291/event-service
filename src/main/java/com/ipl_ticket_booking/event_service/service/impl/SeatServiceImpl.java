package com.ipl_ticket_booking.event_service.service.impl;

import com.ipl_ticket_booking.event_service.common.dto.PageResponse;
import com.ipl_ticket_booking.event_service.common.util.PageUtils;
import com.ipl_ticket_booking.event_service.dto.response.SeatResponse;
import com.ipl_ticket_booking.event_service.mapper.SeatMapper;
import com.ipl_ticket_booking.event_service.repository.SeatRepository;
import com.ipl_ticket_booking.event_service.service.EventService;
import com.ipl_ticket_booking.event_service.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final SeatMapper seatMapper;
    private final EventService eventService;

    @Override
    public PageResponse<SeatResponse> getSeatsByEvent(
            Long eventId,
            Pageable pageable) {

        // Validate event exists
        eventService.findEventEntityById(eventId);

        Page<SeatResponse> page = seatRepository
                .findByEventId(eventId, pageable)
                .map(seatMapper::toResponse);

        return PageUtils.toPageResponse(page);
    }
}