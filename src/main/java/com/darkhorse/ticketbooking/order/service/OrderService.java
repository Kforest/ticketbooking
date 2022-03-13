package com.darkhorse.ticketbooking.order.service;

import com.darkhorse.ticketbooking.order.constants.Message;
import com.darkhorse.ticketbooking.order.controller.dto.OrderCreateRequestDTO;
import com.darkhorse.ticketbooking.order.exception.NoAvailableSeatException;
import com.darkhorse.ticketbooking.order.exception.SeatBookServiceUnavailableException;
import com.darkhorse.ticketbooking.order.gateway.FlightReportGateway;
import com.darkhorse.ticketbooking.order.gateway.SeatBookingGateway;
import com.darkhorse.ticketbooking.order.gateway.dto.FlightReportRequestDTO;
import com.darkhorse.ticketbooking.order.gateway.dto.SeatBookingRequestDTO;
import com.darkhorse.ticketbooking.order.gateway.dto.SeatBookingResponseDTO;
import com.darkhorse.ticketbooking.order.messagequeue.dto.FlightReportMessage;
import com.darkhorse.ticketbooking.order.messagequeue.FlightReportQueue;
import com.darkhorse.ticketbooking.order.model.Order;
import com.darkhorse.ticketbooking.order.model.OrderStatus;
import com.darkhorse.ticketbooking.order.repository.OrderRepository;
import com.darkhorse.ticketbooking.order.gateway.dto.SeatBookingCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final SeatBookingGateway seatBookingGateway;

    private final OrderRepository orderRepository;

    private final FlightReportGateway flightReportGateway;

    private final FlightReportQueue flightReportQueue;

    private static final boolean SUCCESS = true;

    public boolean createOrder(String flightId, OrderCreateRequestDTO request) {
        tryBookSeat(flightId, request);
        Order createdOrder = orderRepository.createOrder(buildDraftOrder(flightId, request));
        tryReportFlightData(flightId, createdOrder);
        return SUCCESS;
    }

    private void tryBookSeat(String flightId, OrderCreateRequestDTO request) {
        SeatBookingResponseDTO responseDTO;
        try {
            responseDTO = seatBookingGateway
                    .bookSeat(new SeatBookingRequestDTO(flightId, request.buildPassengerIdCardNumbers()));
        } catch (Exception exception) {
            log.error("Unable to communicate with seat book service. Terminate order creation process.");
            log.error(exception.getMessage(), exception);
            throw new SeatBookServiceUnavailableException(Message.SEAT_LOCK_ERROR_DETAIL);
        }
        if (SeatBookingCode.NO_MORE_SEAT.equals(responseDTO.getCode())) {
            log.error("Not able to book seat due to no more seat available.");
            throw new NoAvailableSeatException(Message.LOCK_SEAT_FAILED);
        }
    }

    private void tryReportFlightData(String flightId, Order createdOrder) {
        try {
            FlightReportRequestDTO reportRequestDTO = FlightReportRequestDTO.builder()
                    .orderId(createdOrder.getId())
                    .newFlightId(flightId)
                    .orderStatus(createdOrder.getOrderStatus().name())
                    .build();
            flightReportGateway.reportFlight(reportRequestDTO);
        } catch (Exception exception) {
            log.error("Failed to report directly. Turn report request to message.");
            FlightReportMessage message = FlightReportMessage.builder()
                    .flightId(flightId)
                    .orderId(createdOrder.getId())
                    .orderStatus(createdOrder.getOrderStatus().name())
                    .build();
            flightReportQueue.sendFlightReport(message);
        }

     }

    private Order buildDraftOrder(String flightId, OrderCreateRequestDTO request) {
        return Order.builder()
                .flightId(flightId)
                .orderStatus(OrderStatus.UNPAID)
                .passengers(request.buildPassengers())
                .build();
    }
}
