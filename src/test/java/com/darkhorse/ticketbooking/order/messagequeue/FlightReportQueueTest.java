package com.darkhorse.ticketbooking.order.messagequeue;

import com.darkhorse.ticketbooking.base.BaseContainerTest;
import com.darkhorse.ticketbooking.order.messagequeue.dto.FlightReportMessage;
import com.darkhorse.ticketbooking.order.utils.JSONUtils;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FlightReportQueueTest extends BaseContainerTest {

    @Autowired
    FlightReportQueue flightReportQueue;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    void should_call_rabbit_template_send_message_when_send_flight_report() {
        FlightReportMessage flightReportMessage = FlightReportMessage.builder()
                .orderId("orderId")
                .flightId("flightId")
                .orderStatus("UNPAID")
                .build();
        flightReportQueue.sendFlightReport(flightReportMessage);
        rabbitTemplate.setExchange(QueueConfiguration.EXCHANGE_NAME);
        Object order = rabbitTemplate.receiveAndConvert("order");
        FlightReportMessage retrievedMessage = JSONUtils.stringToObject((String)order, FlightReportMessage.class);
        assertEquals(flightReportMessage, retrievedMessage);
    }
}