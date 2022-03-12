package com.darkhorse.ticketbooking.order.gateway;

import com.darkhorse.ticketbooking.order.gateway.dto.FlightReportRequestDTO;
import com.darkhorse.ticketbooking.order.gateway.dto.FlightReportResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FlightReportGateway {

    private final FlightReportClient flightReportClient;

    public FlightReportResponseDTO reportFlight(FlightReportRequestDTO request) {
        return flightReportClient.reportFlight(request);
    }
}
