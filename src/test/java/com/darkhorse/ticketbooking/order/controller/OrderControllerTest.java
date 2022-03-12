package com.darkhorse.ticketbooking.order.controller;

import com.darkhorse.ticketbooking.common.JSONUtils;
import com.darkhorse.ticketbooking.order.constants.Message;
import com.darkhorse.ticketbooking.order.controller.dto.OrderCreateRequestControllerDTO;
import com.darkhorse.ticketbooking.order.controller.dto.PassengerControllerDTO;
import com.darkhorse.ticketbooking.order.exception.OrderException;
import com.darkhorse.ticketbooking.order.service.OrderService;
import com.darkhorse.ticketbooking.order.service.contants.SeatBookingCode;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrderService orderService;

    @Test
    void should_return_success_when_create_order_given_create_order_success_in_service() throws Exception {
        Mockito.when(orderService.createOrder(eq("id-success-flight"), any(OrderCreateRequestControllerDTO.class)))
                        .thenReturn(true);
        mvc.perform(post("/flights/id-success-flight/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONUtils.objectToString(prepareOrderCreateRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("SUCCESS")))
                .andExpect(jsonPath("$.message", is("Order Created!")));
    }

    @Test
    void should_return_failure_when_create_order_given_no_available_seats_to_book() throws Exception {
        Mockito.when(orderService.createOrder(anyString(), any(OrderCreateRequestControllerDTO.class)))
                        .thenThrow(new OrderException(SeatBookingCode.NO_MORE_SEAT.name(), Message.LOCK_SEAT_FAILED));

        mvc.perform(post("/flights/id-lock-seat-failed-flight/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONUtils.objectToString(prepareOrderCreateRequest())))
                .andExpect(status().is(409))
                .andExpect(jsonPath("$.code", is("FAILED")))
                .andExpect(jsonPath("$.message", is(Message.LOCK_SEAT_FAILED)));
    }

    @Test
    void should_return_failure_when_create_order_given_seat_book_service_down() throws Exception {

        Mockito.when(orderService.createOrder(anyString(), any(OrderCreateRequestControllerDTO.class)))
                        .thenThrow(new OrderException(Message.SEAT_LOCK_ERROR, Message.SEAT_LOCK_ERROR_DETAIL));

        mvc.perform(post("/flights/id-lock-seat-exception-flight/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONUtils.objectToString(prepareOrderCreateRequest())))
                .andExpect(status().is(500))
                .andExpect(jsonPath("$.code", is("FAILED")))
                .andExpect(jsonPath("$.message", is(Message.SEAT_LOCK_ERROR_DETAIL)));
    }

    private OrderCreateRequestControllerDTO prepareOrderCreateRequest() {
        return OrderCreateRequestControllerDTO
                .builder()
                .passengers(List.of(PassengerControllerDTO.builder()
                        .name("passenger1")
                        .idCardNumber("passenger1")
                        .build()))
                .build();
    }

}