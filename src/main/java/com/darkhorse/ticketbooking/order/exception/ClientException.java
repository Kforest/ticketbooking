package com.darkhorse.ticketbooking.order.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class ClientException extends RuntimeException {
}
