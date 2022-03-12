package com.darkhorse.ticketbooking.order.repository.po;

import com.darkhorse.ticketbooking.order.model.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class OrderPO {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id")
    private String id;

    @Column(name = "flight_id")
    private String flightId;

    @Column(name = "status")
    private String status;

    @Column(name = "create_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createAt;

    @Column(name = "update_at", updatable = false)
    @UpdateTimestamp
    private LocalDateTime updateAt;

    public static OrderPO createBy(Order toBeSavedOrder) {
        return OrderPO.builder()
                .flightId(toBeSavedOrder.getFlightId())
                .status(toBeSavedOrder.getOrderStatus().name())
                .build();
    }

}
