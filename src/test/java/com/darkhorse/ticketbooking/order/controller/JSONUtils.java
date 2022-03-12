package com.darkhorse.ticketbooking.order.controller;

import com.darkhorse.ticketbooking.order.controller.dto.OrderCreateRequestControllerDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JSONUtils {
    private JSONUtils() {
    }

    public static String getRequestBody(OrderCreateRequestControllerDTO request) {
        try {
            return new ObjectMapper().writeValueAsString(request);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }
}