package com.wildan.e_commerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long paymentId;

    private String paymentMethod;
    private String status;
    private Double amountPaid;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
