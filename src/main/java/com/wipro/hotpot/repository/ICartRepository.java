package com.wipro.hotpot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wipro.hotpot.entity.Cart;
import com.wipro.hotpot.entity.User;

@Repository
public interface ICartRepository extends JpaRepository<Cart, Long> {

	Optional<Cart> findByUser(User user);

	Optional<Cart> findByUserId(Long userId);

	boolean existsByUserId(Long userId);
}