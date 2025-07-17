package com.wildan.e_commerce.controller;

import com.wildan.e_commerce.config.AppConstant;
import com.wildan.e_commerce.payload.CategoryDTO;
import com.wildan.e_commerce.payload.CategoryResponse;
import com.wildan.e_commerce.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<CategoryResponse> getAllCategory(
            @RequestParam(name = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = "categoryId", required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstant.SORT_DIR, required = false) String sortOrder
    ) {
        CategoryResponse categoryResponse = categoryService.getAllCategory(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(categoryResponse , HttpStatus.OK);
    }

    @PostMapping("/categories")
    public  ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO){
        CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(createdCategory , HttpStatus.CREATED);
    }

    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@RequestBody CategoryDTO categoryDTO , @PathVariable Long categoryId){
        CategoryDTO updatedCategory = categoryService.updateCategory(categoryDTO, categoryId);
        return new ResponseEntity<>(updatedCategory , HttpStatus.OK);
    }

    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deletedCategory(@PathVariable Long categoryId){
        CategoryDTO deletedCategory = categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(deletedCategory , HttpStatus.OK);
    }


}
