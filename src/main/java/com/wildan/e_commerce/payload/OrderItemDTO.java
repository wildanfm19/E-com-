package com.wildan.e_commerce.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDTO {

    private Long orderItemId;

    private Long productId;
    private String productName;
    private Double productPrice;
    private Integer quantity;
}
