package com.ipl_ticket_booking.event_service.entity;

import com.ipl_ticket_booking.event_service.common.entity.BaseEntity;
import com.ipl_ticket_booking.event_service.entity.EventStatus;
import com.ipl_ticket_booking.event_service.entity.Venue;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "venue_id", nullable = false)
    private Venue venue;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private LocalDateTime eventDateTime;

    @Column(nullable = false)
    private LocalDateTime bookingStart;

    @Column(nullable = false)
    private LocalDateTime bookingEnd;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventStatus status;
}