package com.wildan.e_commerce.services;

import com.wildan.e_commerce.payload.CartDTO;

public interface CartService {
    CartDTO addItemToCart(Long productId , Integer quantity);

    CartDTO getCartById();

    String deleteProductFromCart(Long productId);

    CartDTO updateProductQuantityInCart(Long productId , Integer quantity);
}
