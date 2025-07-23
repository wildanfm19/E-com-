package com.wildan.e_commerce.services;

import com.wildan.e_commerce.exceptions.APIException;
import com.wildan.e_commerce.exceptions.ResourceNotFoundException;
import com.wildan.e_commerce.model.Cart;
import com.wildan.e_commerce.model.CartItem;
import com.wildan.e_commerce.model.Product;
import com.wildan.e_commerce.payload.CartDTO;
import com.wildan.e_commerce.payload.CartItemDTO;
import com.wildan.e_commerce.payload.ProductDTO;
import com.wildan.e_commerce.payload.ProductResponse;
import com.wildan.e_commerce.repositories.CartItemRepository;
import com.wildan.e_commerce.repositories.CartRepository;
import com.wildan.e_commerce.repositories.ProductRepository;
import com.wildan.e_commerce.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CartServiceImpl  implements  CartService{

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

    private final AuthUtil authUtil;

    private final ProductRepository productRepository;

    private final ModelMapper modelMapper;


    @Override
    public CartDTO addItemToCart(Long productId, Integer quantity) {
        Cart cart = getOrCreateCart();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product" , "productId" , productId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCart(cart.getCartId(), productId);

        if(cartItem != null){
            throw new APIException("Product with name : " + product.getName() + " already exist!!");
        }

        if(product.getQuantity() == 0){
            throw  new APIException("Product not available!!");
        }

        if(product.getQuantity() < quantity){
            throw new APIException("Please make an order of this product : " + product.getName()
                    + " less or equal than " + product.getQuantity());
        }

        CartItem newCartItem = CartItem.builder()
                .product(product)
                .quantity(quantity)
                .productPrice(product.getPrice())
                .cart(cart)
                .build();

        cartItemRepository.save(newCartItem);

        cart.setTotalPrice(cart.getTotalPrice() + newCartItem.getProductPrice() * quantity);
        cart.getCartItems().add(newCartItem);
        cartRepository.save(cart);

       CartDTO cartDTO = modelMapper.map(cart , CartDTO.class);

        List<CartItem> cartItems = cart.getCartItems();

        Stream<ProductDTO> productStream = cartItems.stream().map(item -> {
            ProductDTO map = modelMapper.map(item.getProduct() , ProductDTO.class);
            map.setQuantity(item.getQuantity());
            return map;
        });

        cartDTO.setProducts(productStream.toList());

        return cartDTO;
    }

    public Cart getOrCreateCart(){
        Cart existCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());

        if(existCart != null){
            return existCart;
        }

        Cart cart = new Cart();
        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loggedInUser());

        return cartRepository.save(cart);

    }



}
