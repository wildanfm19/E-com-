package com.wildan.e_commerce.controller;

import com.wildan.e_commerce.config.AppConstant;
import com.wildan.e_commerce.model.Payment;
import com.wildan.e_commerce.payload.OrderResponse;
import com.wildan.e_commerce.payload.PaymentDTO;
import com.wildan.e_commerce.payload.PaymentResponse;
import com.wildan.e_commerce.repositories.PaymentRepository;
import com.wildan.e_commerce.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mock-payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/gopay/{orderId}")
    public ResponseEntity<PaymentDTO>  createPayment(
            @PathVariable Long orderId,
            @RequestBody PaymentDTO paymentDTO
    ) {
        PaymentDTO payment = paymentService.createPayment(orderId , paymentDTO);
        return new ResponseEntity<>(payment , HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<PaymentResponse> getAllPayment(
            @RequestParam(name = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = "paymentId", required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstant.SORT_DIR, required = false) String sortOrder
    ){
        PaymentResponse allPayment = paymentService.getAllPayment(pageNumber ,pageSize , sortBy , sortOrder);
        return new ResponseEntity<>(allPayment , HttpStatus.OK);
    }

    @GetMapping("/payment/{paymentId}")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable Long paymentId)
    {
        PaymentDTO paymentDTO = paymentService.getPaymentById(paymentId);
        return new ResponseEntity<>(paymentDTO , HttpStatus.OK);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentDTO> getPaymentByOrderId(@PathVariable Long orderId)
    {
        PaymentDTO paymentDTO = paymentService.getPaymentByOrderId(orderId);
        return new ResponseEntity<>(paymentDTO , HttpStatus.OK);
    }

    @GetMapping("/payment/user")
    public ResponseEntity<List<PaymentDTO>> getUserPayment(){
        List<PaymentDTO> userPayment = paymentService.getUserPayment();
        return new ResponseEntity<>(userPayment, HttpStatus.OK);
    }


}
