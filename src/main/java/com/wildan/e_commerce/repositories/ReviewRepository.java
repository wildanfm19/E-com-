package com.wildan.e_commerce.repositories;

import com.wildan.e_commerce.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review , Long> {
}
