package com.wildan.e_commerce.services;

import com.wildan.e_commerce.payload.CategoryResponse;
import com.wildan.e_commerce.payload.ProductDTO;
import com.wildan.e_commerce.payload.ProductResponse;

public interface ProductService {

    ProductResponse getAllProduct(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductDTO createProduct(ProductDTO productDTO , Long categoryId);

    ProductDTO deleteProduct(Long productId);

    ProductDTO updateProduct(ProductDTO productDTO, Long productId);

    ProductResponse getProductByKeyword(String keyword , Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse getProductByCategory(Long categoryId , Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

}
