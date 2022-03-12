package com.darkhorse.ticketbooking.order.gateway.dto;

import com.darkhorse.ticketbooking.order.service.contants.FlightReportCode;
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
public class FlightReportResponseDTO {
    private FlightReportCode code;
}
