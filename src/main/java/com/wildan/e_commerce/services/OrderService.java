package com.wildan.e_commerce.services;

import com.wildan.e_commerce.model.Order;
import com.wildan.e_commerce.payload.OrderDTO;
import com.wildan.e_commerce.payload.OrderResponse;

public interface OrderService {

    OrderDTO placeOrder();

    OrderResponse getUserOrder(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    OrderDTO getOrderById(Long id);

    OrderDTO mapToOrderDTO(Order order);

    OrderResponse getAllOrder(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
}
