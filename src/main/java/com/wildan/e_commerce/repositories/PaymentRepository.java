package com.wildan.e_commerce.repositories;

import com.wildan.e_commerce.model.Order;
import com.wildan.e_commerce.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment , Long> {

  Optional<Payment> findByOrder(Order order);

  @Query("SELECT p FROM Payment p WHERE p.order.user.email = :email")
  List<Payment> findAllByUserEmail(String email);
}
