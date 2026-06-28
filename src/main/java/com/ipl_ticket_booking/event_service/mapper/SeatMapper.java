package com.ipl_ticket_booking.event_service.mapper;

import com.ipl_ticket_booking.event_service.dto.response.SeatResponse;
import com.ipl_ticket_booking.event_service.entity.Seat;
import org.springframework.stereotype.Component;

@Component
public class SeatMapper {

    public SeatResponse toResponse(Seat seat) {

        return SeatResponse.builder()
                .id(seat.getId())
                .seatLabel(seat.getSeatLabel())
                .rowLabel(seat.getRowLabel())
                .seatNumber(seat.getSeatNumber())
                .categoryName(seat.getSeatCategory().getName())
                .build();
    }

}