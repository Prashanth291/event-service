package com.ipl_ticket_booking.event_service.entity;

import com.ipl_ticket_booking.event_service.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "seats",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_event_seat_label",
                        columnNames = {
                                "event_id",
                                "seat_label"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seat_category_id", nullable = false)
    private SeatCategory seatCategory;

    @Column(nullable = false)
    private Character rowLabel;

    @Column(nullable = false)
    private Integer seatNumber;

    @Column(nullable = false, length = 30)
    private String seatLabel;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

}