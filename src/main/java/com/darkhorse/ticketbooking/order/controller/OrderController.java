package com.darkhorse.ticketbooking.order.controller;

import com.darkhorse.ticketbooking.order.common.CommonResponse;
import com.darkhorse.ticketbooking.order.controller.dto.OrderCreateRequestControllerDTO;
import com.darkhorse.ticketbooking.order.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Order controller")
@RestController
@AllArgsConstructor
@RequestMapping
public class OrderController {

    private final OrderService orderService;

    @ApiOperation(value = "create order")
    @PostMapping(value = "/flights/{flightId}/order")
    public ResponseEntity<CommonResponse> createGreeting(
            @PathVariable("flightId") String flightId,
            @RequestBody OrderCreateRequestControllerDTO request
    ) {

        boolean order = orderService.createOrder(flightId, request);
        if (order) {
            return ResponseEntity.ok(CommonResponse.buildBy("SUCCESS", "Order Created!"));
        }
        return null;
    }
}
