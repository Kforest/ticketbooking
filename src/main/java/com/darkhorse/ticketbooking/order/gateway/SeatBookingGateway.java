package com.darkhorse.ticketbooking.order.gateway;

import com.darkhorse.ticketbooking.order.gateway.dto.SeatBookingRequestDTO;
import com.darkhorse.ticketbooking.order.gateway.dto.SeatBookingResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeatBookingGateway {

    private final SeatBookingClient seatBookingClient;

    public SeatBookingResponseDTO bookSeat(SeatBookingRequestDTO request) {
        return seatBookingClient.bookSeat(request);
    }
}
