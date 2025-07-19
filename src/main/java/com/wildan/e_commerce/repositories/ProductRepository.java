package com.wildan.e_commerce.repositories;

import com.wildan.e_commerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product , Long> {

    Product findByName(String productName);
}
