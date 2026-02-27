package com.wipro.hotpot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wipro.hotpot.entity.Category;
import com.wipro.hotpot.entity.Restaurant;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {

	List<Category> findByRestaurant(Restaurant restaurant);

	List<Category> findByRestaurantId(Long restaurantId);

	boolean existsByNameAndRestaurant(String name, Restaurant restaurant);

	Category findByNameAndRestaurantId(String name, Long restaurantId);
}
