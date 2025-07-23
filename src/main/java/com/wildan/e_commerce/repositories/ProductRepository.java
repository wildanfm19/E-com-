package com.wildan.e_commerce.repositories;

import com.wildan.e_commerce.model.Category;
import com.wildan.e_commerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product , Long> {

    Product findByName(String productName);

    Page<Product> findByNameLikeIgnoreCase(String keyword, Pageable pageDetails);

    Page<Product> findByCategoryOrderByPriceAsc(Category category, Pageable pageDetails);
}
