package com.wildan.e_commerce.services;

import com.wildan.e_commerce.payload.CategoryDTO;
import com.wildan.e_commerce.payload.CategoryResponse;

public interface CategoryService {

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryResponse getAllCategory(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    CategoryDTO deleteCategory(Long categoryId);

    CategoryDTO updateCategory(CategoryDTO categoryDTO , Long categoryId);
}
