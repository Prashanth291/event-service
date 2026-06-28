package com.ipl_ticket_booking.event_service.entity;

import com.ipl_ticket_booking.event_service.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "seat_categories",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_event_category_name",
                        columnNames = {"event_id", "name"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatCategory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Character startRow;

    @Column(nullable = false)
    private Character endRow;

    @Column(nullable = false)
    private Integer seatsPerRow;

}