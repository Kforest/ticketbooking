package com.darkhorse.ticketbooking.order.repository;

import com.darkhorse.ticketbooking.base.BaseContainerTest;
import com.darkhorse.ticketbooking.order.model.Order;
import com.darkhorse.ticketbooking.order.model.OrderStatus;
import com.darkhorse.ticketbooking.order.model.Passenger;
import com.darkhorse.ticketbooking.order.repository.po.OrderPO;
import com.darkhorse.ticketbooking.order.repository.po.PassengerPO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderRepositoryTest extends BaseContainerTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderJpaRepository orderJpaRepository;

    @Autowired
    PassengerJpaRepository passengerJpaRepository;

    @Test
    void should_return_saved_order_po_when_create_order() {
        Order toBeSavedOrder = prepareOrder(null, "flightId", OrderStatus.UNPAID);
        String savedOrderId = orderRepository.createOrder(toBeSavedOrder).getId();
        Optional<OrderPO> savedOrderOptional = orderJpaRepository.findById(savedOrderId);
        assertTrue(savedOrderOptional.isPresent());
        OrderPO orderPO = savedOrderOptional.get();
        assertEquals(toBeSavedOrder.getFlightId(), orderPO.getFlightId());
        assertEquals(OrderStatus.UNPAID.name(), orderPO.getStatus());

        List<PassengerPO> passengerPOs = passengerJpaRepository.findAllByOrderId(orderPO.getId());
        assertEquals(1, passengerPOs.size());
        assertEquals("passenger1", passengerPOs.get(0).getName());
        assertEquals("passenger1", passengerPOs.get(0).getIdCardNumber());
    }

    private Order prepareOrder(String orderId, String flightId, OrderStatus status) {
        return Order.builder()
                .id(orderId)
                .flightId(flightId)
                .orderStatus(status)
                .passengers(List.of(new Passenger("passenger1", "passenger1")))
                .build();
    }
}