package com.darkhorse.ticketbooking.order.messagequeue.dto;

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
public class FlightReportMessage {
    private String flightId;
    private String orderId;
    private String orderStatus;
}
