package com.ipl_ticket_booking.event_service.service.impl;

import com.ipl_ticket_booking.event_service.common.dto.PageResponse;
import com.ipl_ticket_booking.event_service.common.exception.DuplicateResourceException;
import com.ipl_ticket_booking.event_service.common.exception.ResourceNotFoundException;
import com.ipl_ticket_booking.event_service.common.util.PageUtils;
import com.ipl_ticket_booking.event_service.dto.request.CreateVenueRequest;
import com.ipl_ticket_booking.event_service.dto.response.VenueResponse;
import com.ipl_ticket_booking.event_service.entity.Venue;
import com.ipl_ticket_booking.event_service.mapper.VenueMapper;
import com.ipl_ticket_booking.event_service.repository.VenueRepository;
import com.ipl_ticket_booking.event_service.service.VenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    @Override
    public PageResponse<VenueResponse> getAllVenues(Pageable pageable) {

        Page<VenueResponse> page =
                venueRepository
                        .findAll(pageable)
                        .map(venueMapper::toResponse);

        return PageUtils.toPageResponse(page);
    }
    @Override
    public VenueResponse getVenueById(Long venueId) {

        Venue venue = findVenueById(venueId);
        return venueMapper.toResponse(venue);
    }

    private Venue findVenueById(Long venueId) {

        return venueRepository.findById(venueId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Venue not found with id: " + venueId
                        ));
    }

    @Override
    public VenueResponse updateVenue(Long venueId,
                                     CreateVenueRequest request) {

        Venue venue = findVenueById(venueId);

        if (venueRepository.existsByNameIgnoreCaseAndIdNot(
                request.getName(),
                venueId)) {

            throw new DuplicateResourceException(
                    "Venue already exists with name: " + request.getName()
            );
        }

        venueMapper.updateEntity(venue, request);
        Venue updatedVenue = venueRepository.save(venue);
        return venueMapper.toResponse(updatedVenue);
    }

    @Override
    public void deleteVenue(Long venueId) {

        Venue venue = findVenueById(venueId);

        venueRepository.delete(venue);
    }

    @Override
    public Venue findVenueEntityById(Long venueId) {
        return findVenueById(venueId);
    }
}