package com.ipl_ticket_booking.event_service.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SeatResponse {

    private Long id;

    private String seatLabel;

    private Character rowLabel;

    private Integer seatNumber;

    private String categoryName;

}