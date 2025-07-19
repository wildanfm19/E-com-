package com.wildan.e_commerce.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
