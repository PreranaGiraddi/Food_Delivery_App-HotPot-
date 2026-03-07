package com.wipro.hotpot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.hotpot.dto.RestaurantDTO;
import com.wipro.hotpot.entity.Restaurant;
import com.wipro.hotpot.entity.User;
import com.wipro.hotpot.exception.ResourceNotFoundException;
import com.wipro.hotpot.repository.IRestaurantRepository;
import com.wipro.hotpot.repository.IUserRepository;

@Service
public class RestaurantServiceImpl implements IRestaurantService {

    @Autowired
    private IRestaurantRepository restaurantRepository;

    @Autowired
    private IUserRepository userRepository;  // ✅ needed to find owner

    // ✅ Fix — addRestaurant with userId + dto
    @Override
    public Restaurant addRestaurant(Long userId, RestaurantDTO dto) {
        User owner = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "User not found with id: " + userId));

        Restaurant restaurant = new Restaurant();
        restaurant.setName(dto.getName());
        restaurant.setLocation(dto.getLocation());
        restaurant.setContactNumber(dto.getContactNumber());
        restaurant.setDescription(dto.getDescription());
        restaurant.setImageUrl(dto.getImageUrl());
        restaurant.setActive(true);
        restaurant.setOwner(owner);  // ✅ set owner

        return restaurantRepository.save(restaurant);
    }

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

    @Override
    public void deleteRestaurant(Long id) {
        Restaurant restaurant = getRestaurantById(id);
        restaurantRepository.delete(restaurant);
    }

    @Override
    public Restaurant getRestaurantById(Long id) {
        return restaurantRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Restaurant not found with id: " + id));
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @Override
    public List<Restaurant> getActiveRestaurants() {
        return restaurantRepository.findByIsActiveTrue();
    }

    @Override
    public List<Restaurant> searchRestaurants(String keyword) {
        return restaurantRepository.searchRestaurants(keyword);
    }

    // ✅ Fix — toggle uses existing isActive value
    @Override
    public Restaurant toggleRestaurantStatus(Long id) {
        Restaurant restaurant = getRestaurantById(id);
        restaurant.setActive(!restaurant.isActive()); // ✅ correct
        return restaurantRepository.save(restaurant);
    }

    // ✅ Fix — ONE method, uses userId
    @Override
    public Restaurant getRestaurantByOwnerId(Long userId) {
        return restaurantRepository.findByOwnerId(userId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "No restaurant found for owner with id: " + userId));
    }
}