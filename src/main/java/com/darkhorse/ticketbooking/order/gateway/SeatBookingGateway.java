package com.darkhorse.ticketbooking.order.gateway;

import com.darkhorse.ticketbooking.order.exception.ClientException;
import com.darkhorse.ticketbooking.order.gateway.dto.SeatBookingRequestDTO;
import com.darkhorse.ticketbooking.order.gateway.dto.SeatBookingResponseDTO;
import com.darkhorse.ticketbooking.order.utils.JSONUtils;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
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
        } catch (Exception exception) {
            log.error("Failed to communicate with Seat Booking 3rd party service.");
            log.error(exception.getMessage(), exception);
        }
        throw new ClientException();
    }
}
