package com.darkhorse.ticketbooking.order.messagequeue;

import com.darkhorse.ticketbooking.order.messagequeue.dto.FlightReportMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class FlightReportQueueTest {

    @InjectMocks
    FlightReportQueue flightReportQueue;

    @Mock
    RabbitTemplate rabbitTemplate;

    @Test
    void should_call_rabbit_template_send_message_when_send_flight_report() {
        FlightReportMessage flightReportMessage = FlightReportMessage.builder()
                .orderId("orderId")
                .flightId("flightId")
                .orderStatus("UNPAID")
                .build();
        flightReportQueue.sendFlightReport(flightReportMessage);
        Mockito.verify(rabbitTemplate, Mockito.times(1))
                .convertAndSend(eq(QueueConfiguration.EXCHANGE_NAME), eq("order.created"), anyString());
    }
}