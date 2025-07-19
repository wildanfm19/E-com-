package com.wildan.e_commerce.controller;

import com.wildan.e_commerce.config.AppConstant;
import com.wildan.e_commerce.model.Product;
import com.wildan.e_commerce.payload.ProductDTO;
import com.wildan.e_commerce.payload.ProductResponse;
import com.wildan.e_commerce.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = "productId", required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstant.SORT_DIR, required = false) String sortOrder
    ){
        ProductResponse productResponse = productService.getAllProduct(pageNumber , pageSize , sortBy , sortOrder);
        return new ResponseEntity<>(productResponse , HttpStatus.OK);
    }

    @PostMapping("/products/{categoryId}")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO , @PathVariable Long categoryId)
    {
        ProductDTO product = productService.createProduct(productDTO , categoryId);
        return new ResponseEntity<>(product , HttpStatus.CREATED);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId)
    {
        ProductDTO deletedProduct = productService.deleteProduct(productId);
        return new ResponseEntity<>(deletedProduct , HttpStatus.OK);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO productDTO , @PathVariable Long productId){
        ProductDTO product = productService.updateProduct(productDTO , productId);
        return new ResponseEntity<>(product , HttpStatus.OK);
    }
}
