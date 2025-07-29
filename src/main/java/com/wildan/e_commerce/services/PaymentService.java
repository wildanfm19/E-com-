package com.wildan.e_commerce.services;

import com.wildan.e_commerce.payload.PaymentDTO;
import com.wildan.e_commerce.payload.PaymentResponse;

import java.util.List;

public interface PaymentService {

    PaymentDTO createPayment(Long orderId , PaymentDTO paymentDTO);

    PaymentDTO getPaymentByOrderId(Long orderId);

    PaymentResponse getAllPayment(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    PaymentDTO getPaymentById(Long paymentId);

    List<PaymentDTO> getUserPayment();
}
