package com.wildan.e_commerce.payload;

import com.wildan.e_commerce.model.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {

    private Long orderId;
    private Double totalPrice;
    private LocalDateTime orderDate;
    private String status;
    private Long userId; // atau email user jika mau

    private List<OrderItemDTO> orderItems;
}
