package com.wildan.e_commerce.repositories;

import com.wildan.e_commerce.model.Order;
import com.wildan.e_commerce.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order , Long> {

    Optional<Order> findByUser(User user);

    Page<Order> findAllByUser(User user, Pageable pageable);
}
