package com.darkhorse.ticketbooking.order.controller;

import com.darkhorse.ticketbooking.order.constants.Message;
import com.darkhorse.ticketbooking.order.controller.dto.OrderCreateRequestDTO;
import com.darkhorse.ticketbooking.order.controller.dto.PassengerRequestDTO;
import com.darkhorse.ticketbooking.order.exception.NoAvailableSeatException;
import com.darkhorse.ticketbooking.order.exception.SeatBookServiceUnavailableException;
import com.darkhorse.ticketbooking.order.service.OrderService;
import com.darkhorse.ticketbooking.order.utils.JSONUtils;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
        Mockito.when(orderService.createOrder(eq("id-success-flight"), any(OrderCreateRequestDTO.class)))
                        .thenReturn(true);
        mvc.perform(post("/flights/id-success-flight/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONUtils.objectToString(prepareOrderCreateRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("SUCCESS")))
                .andExpect(jsonPath("$.message", is("Order Created!")))
                .andDo(print());
    }

    @Test
    void should_return_failure_when_create_order_given_no_available_seats_to_book() throws Exception {
        Mockito.when(orderService.createOrder(anyString(), any(OrderCreateRequestDTO.class)))
                        .thenThrow(new NoAvailableSeatException(Message.LOCK_SEAT_FAILED));

        mvc.perform(post("/flights/id-lock-seat-failed-flight/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONUtils.objectToString(prepareOrderCreateRequest())))
                .andExpect(status().is(409))
                .andExpect(jsonPath("$.code", is("FAILED")))
                .andExpect(jsonPath("$.message", is(Message.LOCK_SEAT_FAILED)))
                .andDo(print());
    }

    @Test
    void should_return_failure_when_create_order_given_seat_book_service_down() throws Exception {

        Mockito.when(orderService.createOrder(anyString(), any(OrderCreateRequestDTO.class)))
                        .thenThrow(new SeatBookServiceUnavailableException(Message.SEAT_LOCK_ERROR_DETAIL));

        mvc.perform(post("/flights/id-lock-seat-exception-flight/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONUtils.objectToString(prepareOrderCreateRequest())))
                .andExpect(status().is(500))
                .andExpect(jsonPath("$.code", is("FAILED")))
                .andExpect(jsonPath("$.message", is(Message.SEAT_LOCK_ERROR_DETAIL)))
                .andDo(print());
    }

    private OrderCreateRequestDTO prepareOrderCreateRequest() {
        return OrderCreateRequestDTO
                .builder()
                .passengers(List.of(PassengerRequestDTO.builder()
                        .name("passenger1")
                        .idCardNumber("passenger1")
                        .build()))
                .build();
    }

}