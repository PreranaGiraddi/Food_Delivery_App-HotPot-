package com.wipro.hotpot.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import com.wipro.hotpot.entity.User;
import org.springframework.stereotype.Service;

import com.wipro.hotpot.dto.RestaurantDTO;
import com.wipro.hotpot.entity.Restaurant;
import com.wipro.hotpot.repository.IRestaurantRepository;
import com.wipro.hotpot.repository.IUserRepository;

@Service
public class RestaurantServiceImpl implements IRestaurantService {

    @Autowired
    private IRestaurantRepository restaurantRepository;

    @Autowired
    private IUserRepository userRepository;

    
    @Override
    public RestaurantDTO addRestaurant(RestaurantDTO dto, Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        Restaurant r = new Restaurant();
        r.setName(dto.getName());
        r.setLocation(dto.getLocation());
        r.setContactNumber(dto.getContactNumber());
        r.setDescription(dto.getDescription());
        r.setImageUrl(dto.getImageUrl());
        r.setOwner(owner);
        r.setActive(true);

        return toDTO(restaurantRepository.save(r));
    }

  
    @Override
    public RestaurantDTO updateRestaurant(Long id, RestaurantDTO dto) {
        Restaurant r = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found: " + id));

        if (dto.getName() != null)          r.setName(dto.getName());
        if (dto.getLocation() != null)      r.setLocation(dto.getLocation());
        if (dto.getContactNumber() != null) r.setContactNumber(dto.getContactNumber());
        if (dto.getDescription() != null)   r.setDescription(dto.getDescription());
        if (dto.getImageUrl() != null)      r.setImageUrl(dto.getImageUrl());

        return toDTO(restaurantRepository.save(r));
    }

   
    @Override
    public RestaurantDTO getRestaurantById(Long id) {
        Restaurant r = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found: " + id));
        return toDTO(r);
    }

    
    @Override
    public RestaurantDTO getRestaurantByOwnerId(Long userId) {
        Restaurant r = restaurantRepository.findByOwnerId(userId)
                .orElseThrow(() -> new RuntimeException(
                    "No restaurant found for user: " + userId));
        return toDTO(r);
    }

    
    @Override
    public List<RestaurantDTO> getAllRestaurants() {
        return restaurantRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

   
    @Override
    public List<RestaurantDTO> getActiveRestaurants() {
        return restaurantRepository.findByIsActiveTrue()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

  
    @Override
    public RestaurantDTO toggleActive(Long id, boolean active) {
        Restaurant r = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found: " + id));
        r.setActive(active);
        return toDTO(restaurantRepository.save(r));
    }

   
    @Override
    public void deleteRestaurant(Long id) {
        if (!restaurantRepository.existsById(id))
            throw new RuntimeException("Restaurant not found: " + id);
        restaurantRepository.deleteById(id);
    }

    
    private RestaurantDTO toDTO(Restaurant r) {
        RestaurantDTO dto = new RestaurantDTO();
        dto.setId(r.getId());
        dto.setName(r.getName());
        dto.setLocation(r.getLocation());
        dto.setContactNumber(r.getContactNumber());
        dto.setDescription(r.getDescription());
        dto.setImageUrl(r.getImageUrl());
        dto.setActive(r.isActive());
        if (r.getOwner() != null) {
            dto.setOwnerId(r.getOwner().getId());
            dto.setOwnerName(r.getOwner().getName());
        }
        return dto;
    }
}