package com.wildan.e_commerce.controller;

import com.wildan.e_commerce.payload.CartDTO;
import com.wildan.e_commerce.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId , @PathVariable Integer quantity){
        CartDTO cart = cartService.addItemToCart(productId , quantity);
        return new ResponseEntity<>(cart , HttpStatus.OK);
    }


}
