package com.ipl_ticket_booking.event_service.entity;

import com.ipl_ticket_booking.event_service.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "venues")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venue extends BaseEntity {

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 100)
    private String state;

    @Column(nullable = false, length = 100)
    private String country;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(nullable = false)
    private int maxCapacity;

    @Column(length = 500)
    private String description;
}