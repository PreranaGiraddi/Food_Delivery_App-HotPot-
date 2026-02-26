package com.hotpot.repository.cart;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hotpot.entity.auth.User;
import com.hotpot.entity.cart.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    // ✅ Find cart by user
    Optional<Cart> findByUser(User user);

    // ✅ Find cart by user id
    Optional<Cart> findByUserId(Long userId);

    // ✅ Check if cart exists for user
    boolean existsByUserId(Long userId);
}