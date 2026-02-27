package com.wipro.hotpot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wipro.hotpot.entity.Category;
import com.wipro.hotpot.entity.Restaurant;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {

	List<Category> findByRestaurant(Restaurant restaurant);

	List<Category> findByRestaurantId(Long restaurantId);

	@Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Category c WHERE c.name = :name AND c.restaurant = :restaurant")
	boolean isCategoryExists(String name, Restaurant restaurant);
	
	Category findByNameAndRestaurantId(String name, Long restaurantId);
}
