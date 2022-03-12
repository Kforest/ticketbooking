package com.darkhorse.ticketbooking.order.service;

import com.darkhorse.ticketbooking.order.controller.dto.OrderCreateRequestControllerDTO;
import com.darkhorse.ticketbooking.order.controller.dto.PassengerControllerDTO;
import com.darkhorse.ticketbooking.order.model.Passenger;
import com.darkhorse.ticketbooking.order.service.contants.FlightReportCode;
import com.darkhorse.ticketbooking.order.gateway.FlightReportGateway;
import com.darkhorse.ticketbooking.order.gateway.SeatBookingGateway;
import com.darkhorse.ticketbooking.order.gateway.dto.FlightReportRequestDTO;
import com.darkhorse.ticketbooking.order.gateway.dto.FlightReportResponseDTO;
import com.darkhorse.ticketbooking.order.gateway.dto.SeatBookingRequestDTO;
import com.darkhorse.ticketbooking.order.gateway.dto.SeatBookingResponseDTO;
import com.darkhorse.ticketbooking.order.model.Order;
import com.darkhorse.ticketbooking.order.model.OrderStatus;
import com.darkhorse.ticketbooking.order.service.contants.SeatBookingCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    OrderRepository orderRepository;

    @Mock
    SeatBookingGateway seatBookingGateway;

    @Mock
    FlightReportGateway flightReportGateway;

    @Test
    void should_return_true_when_create_order_given_book_seat_ok_and_create_order_ok_and_report_order_ok() {
        //given
        //stub seat booking
        SeatBookingRequestDTO seatBookingRequestDTO = SeatBookingRequestDTO.builder()
                .flightId("id-success-flight")
                .idCardNumbers(List.of("passenger1"))
                .build();
        Mockito.when(seatBookingGateway.bookSeat(eq(seatBookingRequestDTO)))
                .thenReturn(new SeatBookingResponseDTO(SeatBookingCode.SUCCESS));
        //stub order create
        Order toBeSavedOrder = prepareOrder("", "id-success-flight", OrderStatus.DRAFT);
        Order unPaidOrder = prepareOrder("orderId", "id-success-flight", OrderStatus.UNPAID);
        Mockito.when(orderRepository.createOrder(eq(toBeSavedOrder)))
                .thenReturn(unPaidOrder);
        //stub order report
        FlightReportRequestDTO reportRequestDTO = FlightReportRequestDTO.builder()
                .orderId("orderId")
                .newFlightId("id-success-flight")
                .orderStatus(OrderStatus.UNPAID.name())
                .build();
        Mockito.when(flightReportGateway.reportFlight(eq(reportRequestDTO)))
                .thenReturn(new FlightReportResponseDTO(FlightReportCode.SUCCESS));

        //when
        assertTrue(orderService.createOrder("id-success-flight", prepareOrderCreateRequestDTO()));
    }

    private OrderCreateRequestControllerDTO prepareOrderCreateRequestDTO() {
        return OrderCreateRequestControllerDTO
                .builder()
                .passengers(List.of(PassengerControllerDTO.builder()
                        .name("passenger1")
                        .idCardNumber("passenger1")
                        .build()))
                .build();
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