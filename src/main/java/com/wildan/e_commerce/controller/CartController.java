package com.wildan.e_commerce.controller;

import com.wildan.e_commerce.payload.CartDTO;
import com.wildan.e_commerce.services.CartService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
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

    @GetMapping("/carts/user")
    public ResponseEntity<CartDTO> getUserCart(){
        CartDTO cart = cartService.getCartById();
        return new ResponseEntity<>(cart , HttpStatus.OK);
    }

    @DeleteMapping("/carts/products/{productId}")
    public ResponseEntity<String> deleteProductFromCart(@PathVariable Long productId){
        String status = cartService.deleteProductFromCart(productId);
        return new ResponseEntity<>(status , HttpStatus.OK);
    }

    @PutMapping("/carts/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartDTO> updateCartProduct(@PathVariable Long productId ,
                                                     @PathVariable String operation){
        CartDTO cartDTO = cartService.updateProductQuantityInCart(productId ,
                operation.equalsIgnoreCase("delete") ? -1 : 1);

        return new ResponseEntity<>(cartDTO , HttpStatus.OK);
    }

}
