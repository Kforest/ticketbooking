package com.darkhorse.ticketbooking.order.gateway;

import com.darkhorse.ticketbooking.base.BaseContainerTest;
import com.darkhorse.ticketbooking.order.controller.JSONUtils;
import com.darkhorse.ticketbooking.order.gateway.dto.SeatBookingRequestDTO;
import com.darkhorse.ticketbooking.order.gateway.dto.SeatBookingResponseDTO;
import com.darkhorse.ticketbooking.order.service.contants.SeatBookingCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureWireMock(port = 8081)
class SeatBookingGatewayTest extends BaseContainerTest {

    @Autowired
    SeatBookingGateway seatBookingGateway;

    @Test
    void should_get_response_from_third_party() {
        //given:
        //fake seat booking 3rd party service
        SeatBookingRequestDTO requestDTO = SeatBookingRequestDTO.builder()
                .flightId("flightId")
                .idCardNumbers(List.of("idCardNumber"))
                .build();
        stubFor(post("/seat-bookings")
                .withRequestBody(equalToJson(JSONUtils.objectToString(requestDTO), true, true))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(200)
                        .withBody(JSONUtils.objectToString(new SeatBookingResponseDTO(SeatBookingCode.SUCCESS)))));

        assertEquals(SeatBookingCode.SUCCESS, seatBookingGateway.bookSeat(requestDTO).getCode());
    }
}