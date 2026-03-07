package com.wipro.hotpot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.wipro.hotpot.entity.Restaurant;

public interface IRestaurantRepository extends JpaRepository<Restaurant, Long> {

    // ✅ Find active restaurants
    List<Restaurant> findByIsActiveTrue();

    // ✅ Search by name or location
    @Query("SELECT r FROM Restaurant r WHERE " +
           "LOWER(r.name) LIKE LOWER(CONCAT('%',:keyword,'%')) OR " +
           "LOWER(r.location) LIKE LOWER(CONCAT('%',:keyword,'%'))")
    List<Restaurant> searchRestaurants(@Param("keyword") String keyword);

    // ✅ Find by owner userId — key for dashboard!
    @Query("SELECT r FROM Restaurant r WHERE r.owner.id = :userId")
    Optional<Restaurant> findByOwnerId(@Param("userId") Long userId);
}

