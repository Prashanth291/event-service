package com.ipl_ticket_booking.event_service.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VenueResponse {

    private Long id;

    private String name;

    private String city;

    private String state;

    private String country;

    private String address;

    private int maxCapacity;

    private String description;
}