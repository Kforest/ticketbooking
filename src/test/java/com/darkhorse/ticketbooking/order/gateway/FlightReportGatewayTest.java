package com.darkhorse.ticketbooking.order.gateway;

import com.darkhorse.ticketbooking.base.BaseContainerTest;
import com.darkhorse.ticketbooking.order.exception.ClientException;
import com.darkhorse.ticketbooking.order.gateway.dto.FlightReportCode;
import com.darkhorse.ticketbooking.order.gateway.dto.FlightReportRequestDTO;
import com.darkhorse.ticketbooking.order.gateway.dto.FlightReportResponseDTO;
import com.darkhorse.ticketbooking.order.utils.JSONUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@AutoConfigureWireMock(port = 8082)
class FlightReportGatewayTest extends BaseContainerTest {

    @Autowired
    FlightReportGateway flightReportGateway;

    @Test
    void should_return_success_when_report_flight_given_flight_report_service_return_success() {
        //given:
        //fake flight report 3rd party service
        FlightReportRequestDTO requestDTO = FlightReportRequestDTO.builder()
                .orderId("orderId")
                .newFlightId("flightId")
                .orderStatus("UNPAID")
                .build();
        stubFor(post("/flight-reports")
                .withRequestBody(equalToJson(JSONUtils.objectToString(requestDTO), true, true))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(200)
                        .withBody(JSONUtils.objectToString(new FlightReportResponseDTO(FlightReportCode.SUCCESS)))));

        assertEquals(FlightReportCode.SUCCESS, flightReportGateway.reportFlight(requestDTO).getCode());
    }

    @Test
    void should_throw_client_exception_when_report_flight_given_flight_report_service_return_503() {
        //given:
        //fake flight report 3rd party service
        stubFor(post("/flight-reports")
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(503)
                        .withBody(JSONUtils.objectToString(new FlightReportResponseDTO(FlightReportCode.SUCCESS)))));
        assertThrows(ClientException.class, () -> flightReportGateway.reportFlight(new FlightReportRequestDTO()));
    }


}