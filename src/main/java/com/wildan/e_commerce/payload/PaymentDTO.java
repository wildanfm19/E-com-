package com.wildan.e_commerce.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDTO {

    private Long paymentId;
    private String paymentMethod;
    private String status;
    private Double amountPaid;
    private Long orderId;

}
