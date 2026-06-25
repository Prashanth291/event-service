package com.ipl_ticket_booking.event_service.mapper;

import com.ipl_ticket_booking.event_service.dto.request.CreateVenueRequest;
import com.ipl_ticket_booking.event_service.dto.response.VenueResponse;
import com.ipl_ticket_booking.event_service.entity.Venue;
import org.springframework.stereotype.Component;

@Component
public class VenueMapper {

    public Venue toEntity(CreateVenueRequest request) {

        return Venue.builder()
                .name(request.getName())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry())
                .address(request.getAddress())
                .maxCapacity(request.getMaxCapacity())
                .description(request.getDescription())
                .build();
    }

    public VenueResponse toResponse(Venue venue) {

        return VenueResponse.builder()
                .id(venue.getId())
                .name(venue.getName())
                .city(venue.getCity())
                .state(venue.getState())
                .country(venue.getCountry())
                .address(venue.getAddress())
                .maxCapacity(venue.getMaxCapacity())
                .description(venue.getDescription())
                .build();
    }

}