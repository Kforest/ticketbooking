package com.darkhorse.ticketbooking.order.service;

import com.darkhorse.ticketbooking.order.controller.dto.OrderCreateRequestControllerDTO;
import com.darkhorse.ticketbooking.order.gateway.FlightReportGateway;
import com.darkhorse.ticketbooking.order.gateway.SeatBookingGateway;
import com.darkhorse.ticketbooking.order.gateway.dto.FlightReportRequestDTO;
import com.darkhorse.ticketbooking.order.gateway.dto.SeatBookingRequestDTO;
import com.darkhorse.ticketbooking.order.model.Order;
import com.darkhorse.ticketbooking.order.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final SeatBookingGateway seatBookingGateway;

    private final OrderRepository orderRepository;

    private final FlightReportGateway flightReportGateway;

    public boolean createOrder(String flightId, OrderCreateRequestControllerDTO request) {
        tryBookSeat(flightId, request);
        Order createdOrder = orderRepository.createOrder(buildDraftOrder(flightId, request));
        reportFlightData(flightId, createdOrder);
        return true;
    }

    private void tryBookSeat(String flightId, OrderCreateRequestControllerDTO request) {
        SeatBookingRequestDTO seatBookingRequestDTO = SeatBookingRequestDTO.builder()
                .flightId(flightId)
                .idCardNumbers(request.buildPassengerIdCardNumbers())
                .build();
        seatBookingGateway.bookSeat(seatBookingRequestDTO);
    }

    private void reportFlightData(String flightId, Order createdOrder) {
        FlightReportRequestDTO reportRequestDTO = FlightReportRequestDTO.builder()
                .orderId(createdOrder.getId())
                .newFlightId(flightId)
                .orderStatus(createdOrder.getOrderStatus().name())
                .build();
        flightReportGateway.reportFlight(reportRequestDTO);
    }

    private Order buildDraftOrder(String flightId, OrderCreateRequestControllerDTO request) {
        return Order.builder()
                .id("")
                .flightId(flightId)
                .orderStatus(OrderStatus.DRAFT)
                .passengers(request.buildPassengers())
                .build();
    }
}
