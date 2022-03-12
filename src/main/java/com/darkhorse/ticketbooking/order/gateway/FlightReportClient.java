package com.darkhorse.ticketbooking.order.gateway;

import com.darkhorse.ticketbooking.order.gateway.dto.FlightReportRequestDTO;
import com.darkhorse.ticketbooking.order.gateway.dto.FlightReportResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "flightReportClient", url = "${flightReport.url}")
public interface FlightReportClient {
    @PostMapping(value = "/flight-reports")
    FlightReportResponseDTO reportFlight(@RequestBody FlightReportRequestDTO requestDTO);

}
