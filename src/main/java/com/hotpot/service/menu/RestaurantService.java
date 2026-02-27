package com.hotpot.service.menu;

import com.hotpot.dto.menu.RestaurantDTO;
import com.hotpot.entity.menu.Restaurant;
import java.util.List;

public interface RestaurantService {

    Restaurant addRestaurant(RestaurantDTO dto, Long userId);

    Restaurant getRestaurantById(Long id);

    Restaurant getRestaurantByOwnerId(Long ownerId);

    List<Restaurant> getAllRestaurants();

    List<Restaurant> getAllActiveRestaurants();

    Restaurant updateRestaurant(Long id, RestaurantDTO dto);

    void deleteRestaurant(Long id);

    List<Restaurant> searchRestaurants(String keyword);
}
