package com.wildan.e_commerce.services;

import com.wildan.e_commerce.exceptions.APIException;
import com.wildan.e_commerce.exceptions.ResourceNotFoundException;
import com.wildan.e_commerce.model.Order;
import com.wildan.e_commerce.model.Payment;
import com.wildan.e_commerce.model.User;
import com.wildan.e_commerce.payload.OrderDTO;
import com.wildan.e_commerce.payload.PaymentDTO;
import com.wildan.e_commerce.payload.PaymentResponse;
import com.wildan.e_commerce.repositories.OrderRepository;
import com.wildan.e_commerce.repositories.PaymentRepository;
import com.wildan.e_commerce.repositories.UserRepository;
import com.wildan.e_commerce.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements  PaymentService{

    private final PaymentRepository paymentRepository;

    private final OrderRepository orderRepository;

    private final ModelMapper modelMapper;

    private final AuthUtil authUtil;

    private final UserRepository userRepository;


    @Override
    public PaymentDTO createPayment(Long orderId , PaymentDTO paymentDTO) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", paymentDTO.getOrderId()));

        Payment payment = Payment.builder()
                .paymentMethod(paymentDTO.getPaymentMethod())
                .order(order)
                .amountPaid(order.getTotalPrice())
                .status("PAID")
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        return PaymentDTO.builder()
                .paymentId(savedPayment.getPaymentId())
                .paymentMethod(payment.getPaymentMethod())
                .orderId(payment.getOrder().getOrderId())
                .amountPaid(payment.getAmountPaid())
                .status(payment.getStatus())
                .build();

    }

    @Override
    public PaymentDTO getPaymentByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order" , "orderId" , orderId));

        Payment payment = paymentRepository.findByOrder(order)
                .orElseThrow(() -> new ResourceNotFoundException("Payment" , "orderId" , orderId));

        return PaymentDTO.builder()
                .paymentId(payment.getPaymentId())
                .paymentMethod(payment.getPaymentMethod())
                .orderId(payment.getOrder().getOrderId())
                .amountPaid(payment.getAmountPaid())
                .status(payment.getStatus())
                .build();
    }

    @Override
    public PaymentResponse getAllPayment(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber , pageSize , sortByAndOrder);

        Page<Payment> paymentPage = paymentRepository.findAll(pageable);
        List<Payment> payments = paymentPage.getContent();

        List<PaymentDTO> paymentDTOS = payments.stream()
                .map(payment -> modelMapper.map(payment, PaymentDTO.class))
                .toList();

        return PaymentResponse.builder()
                .content(paymentDTOS)
                .pageNumber(paymentPage.getNumber())
                .pageSize(paymentPage.getSize())
                .totalElements(paymentPage.getTotalElements())
                .totalpages(paymentPage.getTotalPages())
                .lastPage(paymentPage.isLast())
                .build();
    }

    @Override
    public PaymentDTO getPaymentById(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment" , "paymentId" , paymentId));

        return PaymentDTO.builder()
                .paymentId(payment.getPaymentId())
                .paymentMethod(payment.getPaymentMethod())
                .orderId(payment.getOrder().getOrderId())
                .amountPaid(payment.getAmountPaid())
                .status(payment.getStatus())
                .build();
    }

    @Override
    public List<PaymentDTO> getUserPayment() {
        String email = authUtil.loggedInEmail();

        User user =  userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User" , "email" , email));

        List<Payment> payments = paymentRepository.findAllByUserEmail(email);

        return payments.stream()
                .map(payment -> PaymentDTO.builder()
                        .paymentId(payment.getPaymentId())
                        .paymentMethod(payment.getPaymentMethod())
                        .status(payment.getStatus())
                        .amountPaid(payment.getAmountPaid())
                        .orderId(payment.getOrder().getOrderId())
                        .build()).toList();



    }
}
