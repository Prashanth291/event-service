package com.ipl_ticket_booking.event_service.service;

import com.ipl_ticket_booking.event_service.common.dto.PageResponse;
import com.ipl_ticket_booking.event_service.dto.request.CreateVenueRequest;
import com.ipl_ticket_booking.event_service.dto.response.VenueResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VenueService {

    VenueResponse createVenue(CreateVenueRequest request);
    PageResponse<VenueResponse> getAllVenues(Pageable pageable);
    VenueResponse getVenueById(Long venueId);
    VenueResponse updateVenue(Long venueId, CreateVenueRequest request);
    void deleteVenue(Long venueId);
}