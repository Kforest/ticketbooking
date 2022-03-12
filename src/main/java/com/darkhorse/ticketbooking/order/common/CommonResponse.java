package com.darkhorse.ticketbooking.order.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse {
    private String code;
    private String message;

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILED = "FAILED";
    private static final String SUCCESS_MESSAGE = "Order Created!";

    public static CommonResponse failed(String message) {
        return new CommonResponse(FAILED, message);
    }

    public static CommonResponse success(String message) {
        return new CommonResponse(SUCCESS, message);
    }
}
