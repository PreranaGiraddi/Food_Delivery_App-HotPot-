package com.wipro.hotpot.repository;

import com.wipro.hotpot.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {

    
    List<Category> findByRestaurantId(Long restaurantId);

    
    boolean existsByNameAndRestaurantId(String name, Long restaurantId);
}