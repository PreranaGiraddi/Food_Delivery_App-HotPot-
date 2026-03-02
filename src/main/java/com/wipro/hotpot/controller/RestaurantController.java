package com.wipro.hotpot.controller;

import com.wipro.hotpot.dto.RestaurantDTO;
import com.wipro.hotpot.entity.Restaurant;
import com.wipro.hotpot.service.IRestaurantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {

    @Autowired
    private IRestaurantService restaurantService;

    // Add new restaurant
    @PostMapping("/add")
    public ResponseEntity<Restaurant> addRestaurant(@Valid @RequestBody RestaurantDTO dto,
                                                    @RequestParam Long userId) {
        Restaurant restaurant = restaurantService.addRestaurant(dto, userId);
        return new ResponseEntity<>(restaurant, HttpStatus.CREATED);
    }

    //Get restaurant by id
    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long id) {
        Restaurant restaurant = restaurantService.getRestaurantById(id);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    // Get restaurant by owner id
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<Restaurant> getRestaurantByOwnerId(@PathVariable Long ownerId) {
        Restaurant restaurant = restaurantService.getRestaurantByOwnerId(ownerId);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    // Get all restaurants
    @GetMapping("/all")
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

    // Get all active restaurants
    @GetMapping("/active")
    public ResponseEntity<List<Restaurant>> getAllActiveRestaurants() {
        List<Restaurant> restaurants = restaurantService.getAllActiveRestaurants();
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

    // Update restaurant
    @PutMapping("/update/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable Long id,
                                                       @Valid @RequestBody RestaurantDTO dto) {
        Restaurant restaurant = restaurantService.updateRestaurant(id, dto);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    // Delete restaurant (soft delete)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
        return new ResponseEntity<>("Restaurant deleted successfully!", HttpStatus.OK);
    }

    // Search restaurants by keyword
    @GetMapping("/search")
    public ResponseEntity<List<Restaurant>> searchRestaurants(@RequestParam String keyword) {
        List<Restaurant> restaurants = restaurantService.searchRestaurants(keyword);
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }
}