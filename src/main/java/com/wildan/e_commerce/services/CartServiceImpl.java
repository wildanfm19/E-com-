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

import java.util.ArrayList;
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

        if (cart.getCartItems() == null) {
            cart.setCartItems(new ArrayList<>());
        }
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

    @Override
    public CartDTO getCartById() {
        String email = authUtil.loggedInEmail();
        Cart cart = cartRepository.findCartByEmail(email);
        if(cart == null){
            throw new ResourceNotFoundException("Cart" , "email" , email);
        }

        CartDTO cartDTO = modelMapper.map(cart , CartDTO.class);
        cart.getCartItems().forEach(c ->
                c.getProduct().setQuantity(c.getQuantity()));
        List<ProductDTO> products = cart.getCartItems().stream()
                .map(p -> modelMapper.map(p.getProduct() , ProductDTO.class))
                .toList();
        cartDTO.setProducts(products);
        System.out.println("CartDTO : " + cartDTO);
        return cartDTO;

    }

    @Override
    public String deleteProductFromCart(Long productId) {
        String email = authUtil.loggedInEmail();
        Cart cart = cartRepository.findCartByEmail(email);

        if(cart == null){
            throw new ResourceNotFoundException("Cart" , "email" , email);
        }
        Long cartId = cart.getCartId();

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCart(cartId , productId);

        if(cartItem == null){
            throw new  ResourceNotFoundException("Product" , "productId" , productId);
        }

        cart.setTotalPrice(cart.getTotalPrice()
            - cartItem.getProductPrice() * cartItem.getQuantity()
        );

        cart.getCartItems().removeIf(item -> item.getProduct().getProductId().equals(productId));

        cartItemRepository.deleteCartItemByProductIdAndCart(cartId,productId);

        cartRepository.save(cart);

        return "Product : " + cartItem.getProduct().getName() + " has been deleted from cart!!";



    }


    @Override
    public CartDTO updateProductQuantityInCart(Long productId, Integer quantity) {
        String email = authUtil.loggedInEmail();
        Cart userCart = cartRepository.findCartByEmail(email);
        Long cartId = userCart.getCartId();

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        if (product.getQuantity() == 0) {
            throw new APIException(product.getName() + " is not available");
        }

        // ✅ FIXED: Check combined cart quantity
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCart(cartId, productId);
        int existingQuantity = (cartItem != null) ? cartItem.getQuantity() : 0;
        int newQuantity = existingQuantity + quantity;

        if (newQuantity > product.getQuantity()) {
            throw new APIException("Cannot add more than available stock. Only " + product.getQuantity() + " available.");
        }

        if (cartItem == null) {
            throw new APIException("Product " + product.getName() + " not available in cart!");
        }

        if (newQuantity < 0) {
            throw new APIException("The resulting quantity cannot be negative");
        }

        if (newQuantity == 0) {
            deleteProductFromCart(productId);
        } else {
            cartItem.setProductPrice(product.getPrice());
            cartItem.setQuantity(newQuantity);
            cart.setTotalPrice(cart.getTotalPrice() + (product.getPrice() * quantity));
            cartRepository.save(cart);
            cartItemRepository.save(cartItem);
        }

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        List<CartItem> cartItems = cart.getCartItems();
        Stream<ProductDTO> productDTOStream = cartItems.stream().map(item -> {
            ProductDTO prd = modelMapper.map(item.getProduct(), ProductDTO.class);
            prd.setQuantity(item.getQuantity());
            return prd;
        });

        cartDTO.setProducts(productDTOStream.toList());
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
