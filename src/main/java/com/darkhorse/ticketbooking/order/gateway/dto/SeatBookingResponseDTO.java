package com.darkhorse.ticketbooking.order.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SeatBookingResponseDTO {
    private SeatBookingCode code;
}
