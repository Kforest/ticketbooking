package com.darkhorse.ticketbooking.order.controller.dto;

import com.darkhorse.ticketbooking.order.model.Passenger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderCreateRequestDTO {
    private List<PassengerRequestDTO> passengers;

    public List<Passenger> buildPassengers() {
        return this.passengers.stream()
                .map(dto -> new Passenger(dto.getName(), dto.getIdCardNumber()))
                .collect(Collectors.toList());
    }

    public List<String> buildPassengerIdCardNumbers() {
        return this.passengers.stream()
                .map(PassengerRequestDTO::getIdCardNumber)
                .collect(Collectors.toList());
    }
}
