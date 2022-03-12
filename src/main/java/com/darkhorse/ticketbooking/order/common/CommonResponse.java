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
    public static CommonResponse buildBy(String code, String message) {
        return new CommonResponse(code, message);
    }
}
