package com.darkhorse.ticketbooking.order.gateway.dto;

import com.darkhorse.ticketbooking.order.service.contants.SeatBookingCode;
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
