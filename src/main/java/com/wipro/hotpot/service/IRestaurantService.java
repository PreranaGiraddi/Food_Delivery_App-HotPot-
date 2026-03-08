package com.wipro.hotpot.service;

import java.util.List;

import com.wipro.hotpot.dto.RestaurantDTO;

public interface IRestaurantService {
    RestaurantDTO addRestaurant(RestaurantDTO dto, Long userId);
    RestaurantDTO updateRestaurant(Long id, RestaurantDTO dto);
    RestaurantDTO getRestaurantById(Long id);
    RestaurantDTO getRestaurantByOwnerId(Long userId);
    List<RestaurantDTO> getAllRestaurants();
    List<RestaurantDTO> getActiveRestaurants();
    RestaurantDTO toggleActive(Long id, boolean active);
    void deleteRestaurant(Long id);
}