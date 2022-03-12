package com.darkhorse.ticketbooking.order.gateway;

import com.darkhorse.ticketbooking.order.gateway.dto.SeatBookingRequestDTO;
import com.darkhorse.ticketbooking.order.gateway.dto.SeatBookingResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "seatBookingClient", url = "${seatBooking.url}")
public interface SeatBookingClient {

    @PostMapping(value = "/seat-bookings")
    SeatBookingResponseDTO bookSeat(@RequestBody SeatBookingRequestDTO seatBookingRequestDTO);

}
