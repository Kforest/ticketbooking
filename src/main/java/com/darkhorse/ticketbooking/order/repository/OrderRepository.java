package com.darkhorse.ticketbooking.order.repository;

import com.darkhorse.ticketbooking.order.model.Order;
import com.darkhorse.ticketbooking.order.model.OrderStatus;
import com.darkhorse.ticketbooking.order.model.Passenger;
import com.darkhorse.ticketbooking.order.repository.po.OrderPO;
import com.darkhorse.ticketbooking.order.repository.po.PassengerPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final PassengerJpaRepository passengerJpaRepository;

    public Order createOrder(Order toBeSavedOrder) {
        OrderPO savedOrder = orderJpaRepository.save(OrderPO.createBy(toBeSavedOrder));
        List<PassengerPO> toBeSavedPassengerPOs = toBeSavedOrder.getPassengers()
                .stream()
                .map(passenger -> PassengerPO.buildBy(passenger, savedOrder.getId()))
                .collect(Collectors.toList());
        List<PassengerPO> savedPassengerPOs = passengerJpaRepository.saveAll(toBeSavedPassengerPOs);
        return Order.builder()
                .id(savedOrder.getId())
                .orderStatus(Enum.valueOf(OrderStatus.class, savedOrder.getStatus()))
                .flightId(savedOrder.getFlightId())
                .passengers(buildByPOs(savedPassengerPOs))
                .build();
    }

    private List<Passenger> buildByPOs(List<PassengerPO> passengerPOs) {
        return passengerPOs.stream()
                .map(po -> new Passenger(po.getName(), po.getIdCardNumber()))
                .collect(Collectors.toList());
    }
}
