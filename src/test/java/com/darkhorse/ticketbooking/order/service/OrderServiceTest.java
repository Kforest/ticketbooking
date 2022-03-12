package com.darkhorse.ticketbooking.order.service;

import com.darkhorse.ticketbooking.order.constants.Message;
import com.darkhorse.ticketbooking.order.controller.dto.OrderCreateRequestControllerDTO;
import com.darkhorse.ticketbooking.order.controller.dto.PassengerControllerDTO;
import com.darkhorse.ticketbooking.order.exception.OrderException;
import com.darkhorse.ticketbooking.order.model.Passenger;
import com.darkhorse.ticketbooking.order.repository.OrderRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
        Order toBeSavedOrder = prepareOrder(null, "id-success-flight", OrderStatus.UNPAID);
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

    @Test
    void should_throw_exception_when_create_order_given_book_seat_failed_due_unavailable_seat() {
        //given
        //stub seat booking
        Mockito.when(seatBookingGateway.bookSeat(any(SeatBookingRequestDTO.class)))
                .thenReturn(new SeatBookingResponseDTO(SeatBookingCode.NO_MORE_SEAT));
        //when
        OrderException orderException = assertThrows(OrderException.class, () -> orderService.createOrder(anyString(), prepareOrderCreateRequestDTO()));
        assertEquals(SeatBookingCode.NO_MORE_SEAT.name(), orderException.getCode());
        assertEquals(Message.LOCK_SEAT_FAILED, orderException.getMessage());

        //then
        Mockito.verify(orderRepository, Mockito.never()).createOrder(any(Order.class));
        Mockito.verify(flightReportGateway, Mockito.never()).reportFlight(any(FlightReportRequestDTO.class));
    }

    @Test
    void should_throw_exception_when_create_order_given_book_seat_service_is_down() {
        //given
        //stub seat booking
        Mockito.when(seatBookingGateway.bookSeat(any(SeatBookingRequestDTO.class))).thenThrow(new RuntimeException());
        //when
        OrderException orderException = assertThrows(OrderException.class, () -> orderService.createOrder(anyString(), prepareOrderCreateRequestDTO()));
        assertEquals(Message.SEAT_LOCK_ERROR, orderException.getCode());
        assertEquals(Message.SEAT_LOCK_ERROR_DETAIL, orderException.getMessage());
        //then
        Mockito.verify(orderRepository, Mockito.never()).createOrder(any(Order.class));
        Mockito.verify(flightReportGateway, Mockito.never()).reportFlight(any(FlightReportRequestDTO.class));
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