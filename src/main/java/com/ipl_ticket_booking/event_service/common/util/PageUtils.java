package com.ipl_ticket_booking.event_service.common.util;

import com.ipl_ticket_booking.event_service.common.dto.PageResponse;
import org.springframework.data.domain.Page;

public final class PageUtils {

    private PageUtils() {
    }

    public static <T> PageResponse<T> toPageResponse(Page<T> page) {

        return PageResponse.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }
}