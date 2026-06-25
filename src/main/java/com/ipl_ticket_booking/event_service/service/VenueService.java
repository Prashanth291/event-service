package com.ipl_ticket_booking.event_service.service;

import com.ipl_ticket_booking.event_service.dto.request.CreateVenueRequest;
import com.ipl_ticket_booking.event_service.dto.response.VenueResponse;

public interface VenueService {

    VenueResponse createVenue(CreateVenueRequest request);

}