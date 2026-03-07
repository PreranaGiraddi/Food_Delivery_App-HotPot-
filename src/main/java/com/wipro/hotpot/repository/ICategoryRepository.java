package com.wipro.hotpot.repository;

import com.wipro.hotpot.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {

    // ✅ Get all categories for a specific restaurant
    List<Category> findByRestaurantId(Long restaurantId);

    // ✅ Check duplicate name for same restaurant
    boolean existsByNameAndRestaurantId(String name, Long restaurantId);
}