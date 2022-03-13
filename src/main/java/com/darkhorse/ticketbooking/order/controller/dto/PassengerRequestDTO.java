package com.darkhorse.ticketbooking.order.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PassengerRequestDTO {
    private String name;
    private String idCardNumber;
}
