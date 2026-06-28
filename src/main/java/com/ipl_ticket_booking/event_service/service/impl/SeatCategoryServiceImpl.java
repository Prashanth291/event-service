package com.ipl_ticket_booking.event_service.service.impl;

import com.ipl_ticket_booking.event_service.common.dto.PageResponse;
import com.ipl_ticket_booking.event_service.common.exception.BadRequestException;
import com.ipl_ticket_booking.event_service.common.exception.ResourceNotFoundException;
import com.ipl_ticket_booking.event_service.common.util.PageUtils;
import com.ipl_ticket_booking.event_service.dto.request.CreateSeatCategoryRequest;
import com.ipl_ticket_booking.event_service.dto.request.UpdateSeatCategoryRequest;
import com.ipl_ticket_booking.event_service.dto.response.SeatCategoryResponse;
import com.ipl_ticket_booking.event_service.entity.Event;
import com.ipl_ticket_booking.event_service.entity.SeatCategory;
import com.ipl_ticket_booking.event_service.mapper.SeatCategoryMapper;
import com.ipl_ticket_booking.event_service.repository.SeatCategoryRepository;
import com.ipl_ticket_booking.event_service.service.EventService;
import com.ipl_ticket_booking.event_service.service.SeatCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SeatCategoryServiceImpl implements SeatCategoryService {

    private final SeatCategoryRepository seatCategoryRepository;
    private final SeatCategoryMapper seatCategoryMapper;
    private final EventService eventService;

    @Override
    public SeatCategoryResponse createSeatCategory(
            Long eventId,
            CreateSeatCategoryRequest request) {

        Event event = eventService.findEventEntityById(eventId);

        checkDuplicateCategoryName(eventId, request.getName(), null);

        SeatCategory seatCategory =
                seatCategoryMapper.toEntity(request, event);

        validateSeatCategory(seatCategory);

        SeatCategory saved =
                seatCategoryRepository.save(seatCategory);

        return seatCategoryMapper.toResponse(
                findSeatCategoryById(eventId, saved.getId()));
    }

    @Override
    public PageResponse<SeatCategoryResponse> getSeatCategoriesByEvent(
            Long eventId,
            Pageable pageable) {

        eventService.findEventEntityById(eventId);

        Page<SeatCategoryResponse> page =
                seatCategoryRepository.findByEventId(eventId, pageable)
                        .map(seatCategoryMapper::toResponse);

        return PageUtils.toPageResponse(page);
    }

    @Override
    public SeatCategoryResponse getSeatCategoryById(
            Long eventId,
            Long seatCategoryId) {

        return seatCategoryMapper.toResponse(
                findSeatCategoryById(eventId, seatCategoryId)
        );
    }

    @Override
    public SeatCategoryResponse updateSeatCategory(
            Long eventId,
            Long seatCategoryId,
            UpdateSeatCategoryRequest request) {

        SeatCategory seatCategory =
                findSeatCategoryById(eventId, seatCategoryId);

        checkDuplicateCategoryName(
                eventId,
                request.getName(),
                seatCategoryId
        );

        seatCategoryMapper.updateEntity(seatCategory, request);

        validateSeatCategory(seatCategory);

        SeatCategory updated =
                seatCategoryRepository.save(seatCategory);

        return seatCategoryMapper.toResponse(
                findSeatCategoryById(eventId, seatCategoryId)
        );
    }

    @Override
    public void deleteSeatCategory(
            Long eventId,
            Long seatCategoryId) {

        SeatCategory seatCategory =
                findSeatCategoryById(eventId, seatCategoryId);

        seatCategoryRepository.delete(seatCategory);
    }

    @Override
    public SeatCategory findSeatCategoryEntityById(
            Long eventId,
            Long seatCategoryId) {

        return findSeatCategoryById(eventId, seatCategoryId);
    }

    /* ===========================================================
                        PRIVATE HELPER METHODS
       =========================================================== */

    private SeatCategory findSeatCategoryById(
            Long eventId,
            Long seatCategoryId) {

        return seatCategoryRepository
                .findByIdAndEventId(seatCategoryId, eventId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Seat category not found with id: "
                                        + seatCategoryId
                        ));
    }

    private void checkDuplicateCategoryName(
            Long eventId,
            String name,
            Long currentId) {

        boolean exists;

        if (currentId == null) {

            exists = seatCategoryRepository
                    .existsByEventIdAndNameIgnoreCase(
                            eventId,
                            name
                    );

        } else {

            exists = seatCategoryRepository
                    .existsByEventIdAndNameIgnoreCaseAndIdNot(
                            eventId,
                            name,
                            currentId
                    );
        }

        if (exists) {

            throw new BadRequestException(
                    "Seat category with name '" + name +
                            "' already exists for this event."
            );
        }
    }

    private void validateSeatCategory(
            SeatCategory seatCategory) {

        validateRows(
                seatCategory.getStartRow(),
                seatCategory.getEndRow()
        );

        if (seatCategory.getPrice().signum() <= 0) {

            throw new BadRequestException(
                    "Price must be greater than zero."
            );
        }

        if (seatCategory.getSeatsPerRow() <= 0) {

            throw new BadRequestException(
                    "Seats per row must be greater than zero."
            );
        }
    }

    private void validateRows(
            Character startRow,
            Character endRow) {

        char start = Character.toUpperCase(startRow);
        char end = Character.toUpperCase(endRow);

        if (start < 'A' || start > 'Z') {

            throw new BadRequestException(
                    "Invalid start row."
            );
        }

        if (end < 'A' || end > 'Z') {

            throw new BadRequestException(
                    "Invalid end row."
            );
        }

        if (start > end) {

            throw new BadRequestException(
                    "Start row must be before or equal to end row."
            );
        }
    }

}