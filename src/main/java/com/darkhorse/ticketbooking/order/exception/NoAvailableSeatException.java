package com.darkhorse.ticketbooking.order.exception;

public final class NoAvailableSeatException extends OrderException {
    public NoAvailableSeatException(String message) {
        super(message);
    }
}
