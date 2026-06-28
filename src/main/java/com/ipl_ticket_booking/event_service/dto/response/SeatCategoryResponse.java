package com.ipl_ticket_booking.event_service.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class SeatCategoryResponse {

    private Long id;

    private Long eventId;

    private String eventTitle;

    private String name;

    private BigDecimal price;

    private Character startRow;

    private Character endRow;

    private Integer seatsPerRow;

    private Integer totalSeats;

}