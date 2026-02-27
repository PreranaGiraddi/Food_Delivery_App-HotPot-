package com.wipro.hotpot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.hotpot.dto.RestaurantDTO;
import com.wipro.hotpot.entity.Restaurant;
import com.wipro.hotpot.entity.User;
import com.wipro.hotpot.repository.IRestaurantRepository;
import com.wipro.hotpot.repository.IUserRepository;

@Service
public class RestaurantServiceImpl implements IRestaurantService {

	@Autowired
	private IRestaurantRepository restaurantRepository;

	@Autowired
	private IUserRepository userRepository;

	// ✅ Add restaurant
	@Override
	public Restaurant addRestaurant(RestaurantDTO dto, Long userId) {

		User owner = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found!"));

		if (restaurantRepository.existsByName(dto.getName())) {
			throw new RuntimeException("Restaurant name already exists!");
		}

		Restaurant restaurant = new Restaurant();
		restaurant.setName(dto.getName());
		restaurant.setLocation(dto.getLocation());
		restaurant.setContactNumber(dto.getContactNumber());
		restaurant.setDescription(dto.getDescription());
		restaurant.setImageUrl(dto.getImageUrl());
		restaurant.setOwner(owner);
		restaurant.setActive(true);

		return restaurantRepository.save(restaurant);
	}

	// ✅ Get restaurant by id
	@Override
	public Restaurant getRestaurantById(Long id) {
		return restaurantRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + id));
	}

	// ✅ Get restaurant by owner id
	@Override
	public Restaurant getRestaurantByOwnerId(Long ownerId) {
		return restaurantRepository.findByOwnerId(ownerId)
				.orElseThrow(() -> new RuntimeException("Restaurant not found!"));
	}

	// ✅ Get all restaurants
	@Override
	public List<Restaurant> getAllRestaurants() {
		return restaurantRepository.findAll();
	}

	// ✅ Get all active restaurants
	@Override
	public List<Restaurant> getAllActiveRestaurants() {
		return restaurantRepository.findByIsActive(true);
	}

	// ✅ Update restaurant
	@Override
	public Restaurant updateRestaurant(Long id, RestaurantDTO dto) {
		Restaurant restaurant = getRestaurantById(id);
		restaurant.setName(dto.getName());
		restaurant.setLocation(dto.getLocation());
		restaurant.setContactNumber(dto.getContactNumber());
		restaurant.setDescription(dto.getDescription());
		restaurant.setImageUrl(dto.getImageUrl());
		return restaurantRepository.save(restaurant);
	}

	// ✅ Delete restaurant
	@Override
	public void deleteRestaurant(Long id) {
		Restaurant restaurant = getRestaurantById(id);
		restaurant.setActive(false); // soft delete
		restaurantRepository.save(restaurant);
	}

	// ✅ Search restaurants
	@Override
	public List<Restaurant> searchRestaurants(String keyword) {
		return restaurantRepository.searchByName(keyword);
	}

}
