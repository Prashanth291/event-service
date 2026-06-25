package com.ipl_ticket_booking.event_service.service.impl;

import com.ipl_ticket_booking.event_service.common.exception.DuplicateResourceException;
import com.ipl_ticket_booking.event_service.dto.request.CreateVenueRequest;
import com.ipl_ticket_booking.event_service.dto.response.VenueResponse;
import com.ipl_ticket_booking.event_service.entity.Venue;
import com.ipl_ticket_booking.event_service.mapper.VenueMapper;
import com.ipl_ticket_booking.event_service.repository.VenueRepository;
import com.ipl_ticket_booking.event_service.service.VenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VenueServiceImpl implements VenueService {

    private final VenueRepository venueRepository;
    private final VenueMapper venueMapper;

    @Override
    public VenueResponse createVenue(CreateVenueRequest request) {

        if (venueRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateResourceException("Venue already exists.");
        }

        Venue venue = venueMapper.toEntity(request);

        Venue savedVenue = venueRepository.save(venue);

        return venueMapper.toResponse(savedVenue);
    }
}