package com.wildan.e_commerce.services;

import com.wildan.e_commerce.payload.CartDTO;

public interface CartService {
    CartDTO addItemToCart(Long productId , Integer quantity);
}
