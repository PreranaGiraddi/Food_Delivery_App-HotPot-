package com.hotpot.repository.menu;

import com.hotpot.entity.menu.Restaurant;

import com.hotpot.entity.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    // ✅ Find all active restaurants
    List<Restaurant> findByIsActive(boolean isActive);

    // ✅ Find restaurant by owner
    Optional<Restaurant> findByOwner(User owner);

    // ✅ Find restaurant by name
    Optional<Restaurant> findByName(String name);

    // ✅ Check if restaurant name already exists
    boolean existsByName(String name);

    // ✅ Search restaurants by location
    List<Restaurant> findByLocation(String location);

    // ✅ Custom query - search restaurant by name keyword
    @Query("SELECT r FROM Restaurant r WHERE r.name LIKE %:keyword%")
    List<Restaurant> searchByName(String keyword);

    // ✅ Find restaurant by owner id
    @Query("SELECT r FROM Restaurant r WHERE r.owner.id = :ownerId")
    Optional<Restaurant> findByOwnerId(Long ownerId);
}


### 📄 CategoryRepository.java
java
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