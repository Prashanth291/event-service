package com.ipl_ticket_booking.event_service.mapper;

import com.ipl_ticket_booking.event_service.dto.request.CreateSeatCategoryRequest;
import com.ipl_ticket_booking.event_service.dto.request.UpdateSeatCategoryRequest;
import com.ipl_ticket_booking.event_service.dto.response.SeatCategoryResponse;
import com.ipl_ticket_booking.event_service.entity.Event;
import com.ipl_ticket_booking.event_service.entity.SeatCategory;
import org.springframework.stereotype.Component;

@Component
public class SeatCategoryMapper {

    public SeatCategory toEntity(CreateSeatCategoryRequest request, Event event) {

        return SeatCategory.builder()
                .event(event)
                .name(request.getName())
                .price(request.getPrice())
                .startRow(Character.toUpperCase(request.getStartRow()))
                .endRow(Character.toUpperCase(request.getEndRow()))
                .seatsPerRow(request.getSeatsPerRow())
                .build();
    }

    public void updateEntity(SeatCategory seatCategory,
                             UpdateSeatCategoryRequest request) {

        seatCategory.setName(request.getName());
        seatCategory.setPrice(request.getPrice());
        seatCategory.setStartRow(Character.toUpperCase(request.getStartRow()));
        seatCategory.setEndRow(Character.toUpperCase(request.getEndRow()));
        seatCategory.setSeatsPerRow(request.getSeatsPerRow());
    }

    public SeatCategoryResponse toResponse(SeatCategory seatCategory) {

        int totalRows =
                seatCategory.getEndRow() - seatCategory.getStartRow() + 1;

        int totalSeats = totalRows * seatCategory.getSeatsPerRow();

        return SeatCategoryResponse.builder()
                .id(seatCategory.getId())
                .eventId(seatCategory.getEvent().getId())
                .eventTitle(seatCategory.getEvent().getTitle())
                .name(seatCategory.getName())
                .price(seatCategory.getPrice())
                .startRow(seatCategory.getStartRow())
                .endRow(seatCategory.getEndRow())
                .seatsPerRow(seatCategory.getSeatsPerRow())
                .totalSeats(totalSeats)
                .build();
    }

}