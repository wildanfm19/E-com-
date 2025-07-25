package com.wildan.e_commerce.repositories;

import com.wildan.e_commerce.model.Category;
import com.wildan.e_commerce.payload.CategoryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    Category findByName(String categoryName);
}
