package com.wildan.e_commerce.controller;

import com.wildan.e_commerce.config.AppConstant;
import com.wildan.e_commerce.model.Order;
import com.wildan.e_commerce.payload.OrderDTO;
import com.wildan.e_commerce.payload.OrderItemDTO;
import com.wildan.e_commerce.payload.OrderResponse;
import com.wildan.e_commerce.repositories.OrderRepository;
import com.wildan.e_commerce.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/place")
    public ResponseEntity<OrderDTO> placeOrder()
    {
        OrderDTO newOrder = orderService.placeOrder();
        return new ResponseEntity<>(newOrder, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<OrderResponse> getUserOrder(
            @RequestParam(name = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = "orderId", required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstant.SORT_DIR, required = false) String sortOrder
    ){
        OrderResponse userOrder = orderService.getUserOrder(pageNumber ,pageSize , sortBy , sortOrder);
        return new ResponseEntity<>(userOrder , HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id){
        OrderDTO order = orderService.getOrderById(id);
        return new ResponseEntity<>(order , HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<OrderResponse> getAllOrders(
            @RequestParam(name = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = "orderId", required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstant.SORT_DIR, required = false) String sortOrder
    ){
        OrderResponse userOrder = orderService.getAllOrder(pageNumber ,pageSize , sortBy , sortOrder);
        return new ResponseEntity<>(userOrder , HttpStatus.OK);
    }
}
