package com.wipro.hotpot.service;

import java.util.List;

import com.wipro.hotpot.dto.RestaurantDTO;
import com.wipro.hotpot.entity.Restaurant;

public interface IRestaurantService {

	Restaurant addRestaurant(RestaurantDTO dto, Long userId);

	Restaurant getRestaurantById(Long id);

	Restaurant getRestaurantByOwnerId(Long ownerId);

	List<Restaurant> getAllRestaurants();

	List<Restaurant> getAllActiveRestaurants();

	Restaurant updateRestaurant(Long id, RestaurantDTO dto);

	void deleteRestaurant(Long id);

	List<Restaurant> searchRestaurants(String keyword);
}
