package com.wipro.hotpot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wipro.hotpot.entity.Category;
import com.wipro.hotpot.entity.MenuItem;
import com.wipro.hotpot.entity.Restaurant;

@Repository
public interface IMenuRepository extends JpaRepository<MenuItem, Long> {

	List<MenuItem> findByRestaurant(Restaurant restaurant);

	List<MenuItem> findByRestaurantId(Long restaurantId);

	List<MenuItem> findByCategory(Category category);

	List<MenuItem> findByRestaurantIdAndIsAvailable(Long restaurantId, boolean isAvailable);

	List<MenuItem> findByRestaurantIdAndDietaryType(Long restaurantId, MenuItem.DietaryType dietaryType);

	List<MenuItem> findByRestaurantIdAndAvailabilityTime(Long restaurantId, String availabilityTime);

	@Query("SELECT m FROM MenuItem m WHERE m.name LIKE %:keyword% AND m.restaurant.id = :restaurantId")
	List<MenuItem> searchByNameInRestaurant(String keyword, Long restaurantId);

	@Query("SELECT m FROM MenuItem m WHERE m.restaurant.id = :restaurantId AND m.price BETWEEN :minPrice AND :maxPrice")
	List<MenuItem> findByPriceRange(Long restaurantId, Double minPrice, Double maxPrice);

	@Query("SELECT m FROM MenuItem m WHERE m.name LIKE %:keyword%")
	List<MenuItem> globalSearch(String keyword);
}