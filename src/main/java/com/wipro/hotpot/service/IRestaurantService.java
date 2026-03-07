package com.wipro.hotpot.service;

import java.util.List;
import com.wipro.hotpot.dto.RestaurantDTO;
import com.wipro.hotpot.entity.Restaurant;

public interface IRestaurantService {

    // ✅ addRestaurant takes userId + dto
    Restaurant addRestaurant(Long userId, RestaurantDTO dto);

    Restaurant updateRestaurant(Long id, RestaurantDTO dto);

    void deleteRestaurant(Long id);

    Restaurant getRestaurantById(Long id);

    List<Restaurant> getAllRestaurants();

    List<Restaurant> getActiveRestaurants();

    List<Restaurant> searchRestaurants(String keyword);

    Restaurant toggleRestaurantStatus(Long id);

    // ✅ ONE method only — getRestaurantByOwnerId
    Restaurant getRestaurantByOwnerId(Long userId);
}