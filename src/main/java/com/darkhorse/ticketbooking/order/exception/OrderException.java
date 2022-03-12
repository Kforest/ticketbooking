package com.darkhorse.ticketbooking.order.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public final class OrderException extends RuntimeException {

    private final String code;

    public OrderException(String code, String message) {
        super(message);
        this.code = code;
    }
}
