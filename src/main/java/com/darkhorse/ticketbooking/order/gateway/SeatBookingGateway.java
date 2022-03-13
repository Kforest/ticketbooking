package com.darkhorse.ticketbooking.order.gateway;

import com.darkhorse.ticketbooking.order.gateway.dto.SeatBookingRequestDTO;
import com.darkhorse.ticketbooking.order.gateway.dto.SeatBookingResponseDTO;
import com.darkhorse.ticketbooking.order.utils.JSONUtils;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeatBookingGateway {

    private final SeatBookingClient seatBookingClient;

    public SeatBookingResponseDTO bookSeat(SeatBookingRequestDTO request) {
        try {
            return seatBookingClient
                    .bookSeat(request)
                    .getBody();
        } catch (FeignException.FeignClientException exception) {
            if (HttpStatus.CONFLICT.value() == exception.status()) {
                return JSONUtils.stringToObject(exception.contentUTF8(), SeatBookingResponseDTO.class);
            }
        }
        return null;
    }
}
