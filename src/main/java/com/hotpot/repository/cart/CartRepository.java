package com.hotpot.repository.cart;

import com.hotpot.entity.cart.Cart;
import com.hotpot.entity.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    // ✅ Find cart by user
    Optional<Cart> findByUser(User user);

    // ✅ Find cart by user id
    Optional<Cart> findByUserId(Long userId);

    // ✅ Check if cart exists for user
    boolean existsByUserId(Long userId);
}