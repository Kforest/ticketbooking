package com.darkhorse.ticketbooking.order.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponseDTO {
    private String code;
    private String message;

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILED = "FAILED";
    private static final String SUCCESS_MESSAGE = "Order Created!";

    public static CommonResponseDTO failed(String message) {
        return new CommonResponseDTO(FAILED, message);
    }

    public static CommonResponseDTO success(String message) {
        return new CommonResponseDTO(SUCCESS, message);
    }
}
