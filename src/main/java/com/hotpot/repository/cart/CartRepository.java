package com.hotpot.repository.cart;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hotpot.entity.auth.User;
import com.hotpot.entity.cart.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    
    Optional<Cart> findByUser(User user);

    
    Optional<Cart> findByUserId(Long userId);

   
    boolean existsByUserId(Long userId);
}