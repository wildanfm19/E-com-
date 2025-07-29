package com.wildan.e_commerce.services;

import com.wildan.e_commerce.exceptions.APIException;
import com.wildan.e_commerce.exceptions.ResourceNotFoundException;
import com.wildan.e_commerce.model.*;
import com.wildan.e_commerce.payload.OrderDTO;
import com.wildan.e_commerce.payload.OrderItemDTO;
import com.wildan.e_commerce.payload.OrderResponse;
import com.wildan.e_commerce.payload.ProductDTO;
import com.wildan.e_commerce.repositories.CartRepository;
import com.wildan.e_commerce.repositories.OrderRepository;
import com.wildan.e_commerce.repositories.UserRepository;
import com.wildan.e_commerce.utils.AuthUtil;
import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements  OrderService {


    private final OrderRepository orderRepository;

    private final AuthUtil authUtil;

    private final UserRepository userRepository;

    private  final CartRepository cartRepository;

    @Override
    public OrderDTO placeOrder() {

        // Dapetin Email
        String email = authUtil.loggedInEmail();


        // Fetch User dari database
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User" ,"email" , email));

        Cart cart = cartRepository.findCartByEmail(email);
        if(cart == null || cart.getCartItems().isEmpty())
        {
            throw new APIException("Cart is empty , cannot place order!!");
        }

        List<OrderItem> orderItems = cart.getCartItems().stream().map(cartItem ->
            OrderItem.builder()
                    .product(cartItem.getProduct())
                    .quantity(cartItem.getQuantity())
                    .productPrice(cartItem.getProductPrice())
                    .build()
        ).toList();

        // Build Order
        Order order = Order.builder()
                .user(user)
                .status("PLACED")
                .orderItems(orderItems)
                .totalPrice(cart.getTotalPrice())
                .build();


        // Set order for the OrderItems
        orderItems.forEach(item -> item.setOrder(order));


       Order saveOrder =orderRepository.save(order);

       cart.getCartItems().clear();
       cart.setTotalPrice(0.0);
       cartRepository.save(cart);

        OrderDTO orderDTO = OrderDTO.builder()
                .orderId(saveOrder.getOrderId())
                .status(saveOrder.getStatus())
                .totalPrice(saveOrder.getTotalPrice())
                .orderDate(LocalDateTime.parse(saveOrder.getOrderDate().toString()))
                .userId(user.getUserId())
                .orderItems(
                        saveOrder.getOrderItems().stream().map(item ->
                                OrderItemDTO.builder()
                                        .orderItemId(item.getOrderItemId())
                                        .productName(item.getProduct().getName())  // Adjust if product is null-safe
                                        .productPrice(item.getProductPrice())
                                        .productId(item.getProduct().getProductId())
                                        .quantity(item.getQuantity())
                                        .build()
                        ).toList()
                )
                .build();

        return orderDTO;
    }

    @Override
    public OrderResponse getUserOrder(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        String email = authUtil.loggedInEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Order> orderPage = orderRepository.findAllByUser(user, pageable);
        List<Order> orders = orderPage.getContent();

        List<OrderDTO> orderDTOs = orders.stream().map(order -> {
            List<OrderItemDTO> itemDTOs = order.getOrderItems().stream().map(item -> OrderItemDTO.builder()
                    .orderItemId(item.getOrderItemId())
                    .productId(item.getOrderItemId())
                    .productName(item.getProduct().getName())
                    .productPrice(item.getProductPrice())
                    .quantity(item.getQuantity())
                    .build()
            ).toList();

            return OrderDTO.builder()
                    .orderId(order.getOrderId())
                    .userId(user.getUserId())
                    .status(order.getStatus())
                    .totalPrice(order.getTotalPrice())
                    .orderDate(LocalDateTime.parse(order.getOrderDate().toString()))
                    .orderItems(itemDTOs)
                    .build();
        }).toList();

        return OrderResponse.builder()
                .content(orderDTOs)
                .pageNumber(orderPage.getNumber())
                .pageSize(orderPage.getSize())
                .totalElements(orderPage.getTotalElements())
                .totalPages(orderPage.getTotalPages())
                .lastPage(orderPage.isLast())
                .build();
    }

    @Override
    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order" , "orderId" , id));

       return mapToOrderDTO(order);
    }

    @Override
    public OrderDTO mapToOrderDTO(Order order) {
        List<OrderItemDTO> orderItemDTOs = order.getOrderItems().stream().map(item -> {
            return OrderItemDTO.builder()
                    .orderItemId(item.getOrderItemId())
                    .productId(item.getProduct().getProductId())
                    .productName(item.getProduct().getName())
                    .productPrice(item.getProduct().getPrice())
                    .quantity(item.getQuantity())
                    .build();
        }).toList();

        return OrderDTO.builder()
                .orderId(order.getOrderId())
                .totalPrice(order.getTotalPrice())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .userId(order.getUser().getUserId())
                .orderItems(orderItemDTOs)
                .build();
    }

    @Override
    public OrderResponse getAllOrder(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize , sortByAndOrder);

        Page<Order> orderPage = orderRepository.findAll(pageable);
        List<Order> orders = orderPage.getContent();
        List<OrderDTO> orderDTOS = orders.stream()
                .map(this::mapToOrderDTO)
                .toList();

        return OrderResponse.builder()
                .content(orderDTOS)
                .totalPages(orderPage.getTotalPages())
                .totalElements(orderPage.getTotalElements())
                .pageNumber(orderPage.getNumber())
                .pageSize(orderPage.getSize())
                .lastPage(orderPage.isLast())
                .build();
    }
}



