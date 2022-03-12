package com.darkhorse.ticketbooking.order.service;

import com.darkhorse.ticketbooking.order.constants.Message;
import com.darkhorse.ticketbooking.order.controller.dto.OrderCreateRequestControllerDTO;
import com.darkhorse.ticketbooking.order.exception.OrderException;
import com.darkhorse.ticketbooking.order.gateway.FlightReportGateway;
import com.darkhorse.ticketbooking.order.gateway.SeatBookingGateway;
import com.darkhorse.ticketbooking.order.gateway.dto.FlightReportRequestDTO;
import com.darkhorse.ticketbooking.order.gateway.dto.SeatBookingRequestDTO;
import com.darkhorse.ticketbooking.order.gateway.dto.SeatBookingResponseDTO;
import com.darkhorse.ticketbooking.order.model.Order;
import com.darkhorse.ticketbooking.order.model.OrderStatus;
import com.darkhorse.ticketbooking.order.repository.OrderRepository;
import com.darkhorse.ticketbooking.order.service.contants.SeatBookingCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    public static final boolean SUCCESS = true;
    private final SeatBookingGateway seatBookingGateway;

    private final OrderRepository orderRepository;

    private final FlightReportGateway flightReportGateway;

    public boolean createOrder(String flightId, OrderCreateRequestControllerDTO request) {
        tryBookSeat(flightId, request);
        Order createdOrder = orderRepository.createOrder(buildDraftOrder(flightId, request));
        reportFlightData(flightId, createdOrder);
        return SUCCESS;
    }

    private void tryBookSeat(String flightId, OrderCreateRequestControllerDTO request) {
        SeatBookingRequestDTO seatBookingRequestDTO = SeatBookingRequestDTO.builder()
                .flightId(flightId)
                .idCardNumbers(request.buildPassengerIdCardNumbers())
                .build();
        SeatBookingResponseDTO responseDTO = seatBookingGateway.bookSeat(seatBookingRequestDTO);
        if (SeatBookingCode.NO_MORE_SEAT.equals(responseDTO.getCode())) {
            throw new OrderException(responseDTO.getCode().name(), Message.LOCK_SEAT_FAILED);
        }
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
                .flightId(flightId)
                .orderStatus(OrderStatus.UNPAID)
                .passengers(request.buildPassengers())
                .build();
    }
}
