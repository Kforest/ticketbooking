package com.darkhorse.ticketbooking.order.repository;

import com.darkhorse.ticketbooking.order.repository.po.OrderPO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<OrderPO, String> {

}
