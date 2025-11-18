package com.ecommerce_nexus.backend.cart;

import com.ecommerce_nexus.backend.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
    Optional<Cart> findByGuestToken(String guestToken);
}
