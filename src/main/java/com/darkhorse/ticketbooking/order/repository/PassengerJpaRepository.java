package com.darkhorse.ticketbooking.order.repository;

import com.darkhorse.ticketbooking.order.repository.po.PassengerPO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PassengerJpaRepository extends JpaRepository<PassengerPO, String> {
    List<PassengerPO> findAllByOrderId(String orderId);
}
