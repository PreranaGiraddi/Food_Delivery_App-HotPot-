package com.hotpot.repository.menu;

import com.hotpot.entity.menu.Category;
import com.hotpot.entity.menu.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // ✅ Find all categories of a restaurant
    List<Category> findByRestaurant(Restaurant restaurant);

    // ✅ Find all categories by restaurant id
    List<Category> findByRestaurantId(Long restaurantId);

    // ✅ Check if category name exists in a restaurant
    boolean existsByNameAndRestaurant(String name, Restaurant restaurant);

    // ✅ Find category by name and restaurant
    Category findByNameAndRestaurantId(String name, Long restaurantId);
}

