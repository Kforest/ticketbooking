package com.darkhorse.ticketbooking.order.gateway;

import com.darkhorse.ticketbooking.order.exception.ClientException;
import com.darkhorse.ticketbooking.order.gateway.dto.FlightReportRequestDTO;
import com.darkhorse.ticketbooking.order.gateway.dto.FlightReportResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FlightReportGateway {

    private final FlightReportClient flightReportClient;

    public FlightReportResponseDTO reportFlight(FlightReportRequestDTO request) {
        try {
            return flightReportClient.reportFlight(request);
        } catch (Exception exception) {
            log.error("Failed to communicate with flight report 3rd party service.");
            log.error(exception.getMessage(), exception);
        }
        throw new ClientException();
    }
}
