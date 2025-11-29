package com.ecommerce_nexus.backend.order;

import com.ecommerce_nexus.backend.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByCreatedAtDesc(User user);

    boolean existsByTrackingCode(String trackingCode);

    java.util.Optional<Order> findByTrackingCode(String trackingCode);
}
