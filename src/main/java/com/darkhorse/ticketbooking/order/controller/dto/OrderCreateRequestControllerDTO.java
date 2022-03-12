package com.darkhorse.ticketbooking.order.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderCreateRequestControllerDTO {
    private List<PassengerControllerDTO> passengers;
}
