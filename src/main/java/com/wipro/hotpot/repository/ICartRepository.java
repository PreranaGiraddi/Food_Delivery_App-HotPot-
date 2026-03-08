package com.wipro.hotpot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wipro.hotpot.entity.Cart;

@Repository
public interface ICartRepository extends JpaRepository<Cart, Long> {

    
    @Query("SELECT c FROM Cart c WHERE c.user.id = :userId")
    Optional<Cart> findByUserId(@Param("userId") Long userId);

    
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
           "FROM Cart c WHERE c.user.id = :userId")
    boolean isCartExists(@Param("userId") Long userId);
}