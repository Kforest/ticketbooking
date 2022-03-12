package com.darkhorse.ticketbooking.order.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode
public class FlightReportRequestDTO {
    private String orderId;
    private String newFlightId;
    private String orderStatus;
}
