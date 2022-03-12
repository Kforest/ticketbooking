package com.darkhorse.ticketbooking.order.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode
public class Order {
    private String id;
    private String flightId;
    private OrderStatus orderStatus;
    private List<Passenger> passengers;
}
