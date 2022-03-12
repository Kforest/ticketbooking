package com.darkhorse.ticketbooking.order.repository.po;

import com.darkhorse.ticketbooking.order.model.Passenger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

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
@Table(name = "passengers")
public class PassengerPO {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id")
    private String id;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "name")
    private String name;

    @Column(name = "id_card_number")
    private String idCardNumber;

    @Column(name = "create_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createAt;

    public static PassengerPO buildBy(Passenger passenger, String orderId) {
        return PassengerPO.builder()
                .orderId(orderId)
                .name(passenger.getName())
                .idCardNumber(passenger.getIdCardNumber())
                .build();
    }
}
