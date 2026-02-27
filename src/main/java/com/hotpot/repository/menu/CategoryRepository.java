package com.hotpot.repository.menu;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hotpot.entity.menu.Category;
import com.hotpot.entity.menu.Restaurant;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    
    List<Category> findByRestaurant(Restaurant restaurant);

    
    List<Category> findByRestaurantId(Long restaurantId);

    
    boolean existsByNameAndRestaurant(String name, Restaurant restaurant);

   
    Category findByNameAndRestaurantId(String name, Long restaurantId);
}

