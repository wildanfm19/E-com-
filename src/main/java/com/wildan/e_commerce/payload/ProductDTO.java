package com.wildan.e_commerce.payload;

import com.wildan.e_commerce.model.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private String productId;

    private String name;

    private String description;

    private Integer quantity;

    private Double price;

    private String image;

    private List<Review> reviews;
}
