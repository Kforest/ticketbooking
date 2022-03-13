package com.darkhorse.ticketbooking.order.messagequeue;

import com.darkhorse.ticketbooking.order.messagequeue.dto.FlightReportMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FlightReportQueue {

    private final RabbitTemplate rabbitTemplate;

    public void sendFlightReport(FlightReportMessage flightReportMessage) {
        try {
            rabbitTemplate.convertAndSend(QueueConfiguration.EXCHANGE_NAME, "order.created", new ObjectMapper().writeValueAsString(flightReportMessage));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
    }
}
