package com.darkhorse.ticketbooking.order.controller;

import com.darkhorse.ticketbooking.order.controller.dto.CommonResponseDTO;
import com.darkhorse.ticketbooking.order.controller.dto.OrderCreateRequestDTO;
import com.darkhorse.ticketbooking.order.exception.NoAvailableSeatException;
import com.darkhorse.ticketbooking.order.exception.OrderException;
import com.darkhorse.ticketbooking.order.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.contract.spec.internal.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "Order controller")
@RestController
@AllArgsConstructor
@RequestMapping
public class OrderController {

    private final OrderService orderService;

    @ApiOperation(value = "create order")
    @PostMapping(value = "/flights/{flightId}/order")
    public ResponseEntity<CommonResponseDTO> createOrder(
            @PathVariable("flightId") String flightId,
            @RequestBody OrderCreateRequestDTO request
    ) {
        try {
            if (orderService.createOrder(flightId, request)) {
                return ResponseEntity.ok(CommonResponseDTO.success("Order Created!"));
            }
        } catch (OrderException exception) {
            return buildErrorResponse(exception);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonResponseDTO.failed("Failed to create order."));
    }

    private ResponseEntity<CommonResponseDTO> buildErrorResponse(OrderException exception) {
        if (exception instanceof NoAvailableSeatException) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(CommonResponseDTO.failed(exception.getMessage()));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CommonResponseDTO.failed(exception.getMessage()));
        }
    }
}
