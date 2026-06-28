package com.ipl_ticket_booking.event_service.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateSeatCategoryRequest {

    @NotBlank(message = "Category name is required")
    @Size(max = 50)
    private String name;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;

    @NotNull(message = "Start row is required")
    private Character startRow;

    @NotNull(message = "End row is required")
    private Character endRow;

    @NotNull(message = "Seats per row is required")
    @Min(value = 1)
    private Integer seatsPerRow;

}