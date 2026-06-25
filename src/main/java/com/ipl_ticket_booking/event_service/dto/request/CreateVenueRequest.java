package com.ipl_ticket_booking.event_service.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateVenueRequest {

    @NotBlank(message = "Venue name is required")
    @Size(max = 100)
    private String name;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Country is required")
    private String country;

    @NotBlank(message = "Address is required")
    private String address;

    @Positive(message = "Maximum capacity must be greater than zero")
    private int maxCapacity;

    @Size(max = 500)
    private String description;
}