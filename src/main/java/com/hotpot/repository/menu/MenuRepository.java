package com.hotpot.repository.menu;

import com.hotpot.entity.menu.MenuItem;
import com.hotpot.entity.menu.Restaurant;
import com.hotpot.entity.menu.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<MenuItem, Long> {

    // ✅ Find all menu items of a restaurant
    List<MenuItem> findByRestaurant(Restaurant restaurant);

    // ✅ Find all menu items by restaurant id
    List<MenuItem> findByRestaurantId(Long restaurantId);

    // ✅ Find menu items by category
    List<MenuItem> findByCategory(Category category);

    // ✅ Find available menu items of a restaurant
    List<MenuItem> findByRestaurantIdAndIsAvailable(Long restaurantId, boolean isAvailable);

    // ✅ Find menu items by dietary type (VEG / NONVEG)
    List<MenuItem> findByRestaurantIdAndDietaryType(Long restaurantId, MenuItem.DietaryType dietaryType);

    // ✅ Find menu items by availability time (breakfast, lunch, dinner)
    List<MenuItem> findByRestaurantIdAndAvailabilityTime(Long restaurantId, String availabilityTime);

    // ✅ Search menu items by name keyword
    @Query("SELECT m FROM MenuItem m WHERE m.name LIKE %:keyword% AND m.restaurant.id = :restaurantId")
    List<MenuItem> searchByNameInRestaurant(String keyword, Long restaurantId);

    // ✅ Find menu items within price range
    @Query("SELECT m FROM MenuItem m WHERE m.restaurant.id = :restaurantId AND m.price BETWEEN :minPrice AND :maxPrice")
    List<MenuItem> findByPriceRange(Long restaurantId, Double minPrice, Double maxPrice);

    // ✅ Global search across all restaurants
    @Query("SELECT m FROM MenuItem m WHERE m.name LIKE %:keyword%")
    List<MenuItem> globalSearch(String keyword);
}