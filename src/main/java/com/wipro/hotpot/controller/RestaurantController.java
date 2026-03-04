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

  
    @PostMapping("/add")
    public ResponseEntity<Restaurant> addRestaurant(@Valid @RequestBody RestaurantDTO dto,
                                                    @RequestParam Long userId) {
        Restaurant restaurant = restaurantService.addRestaurant(dto, userId);
        return new ResponseEntity<>(restaurant, HttpStatus.CREATED);
    }

  
    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long id) {
        Restaurant restaurant = restaurantService.getRestaurantById(id);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

  
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<Restaurant> getRestaurantByOwnerId(@PathVariable Long ownerId) {
        Restaurant restaurant = restaurantService.getRestaurantByOwnerId(ownerId);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

 
    @GetMapping("/all")
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

  
    @GetMapping("/active")
    public ResponseEntity<List<Restaurant>> getAllActiveRestaurants() {
        List<Restaurant> restaurants = restaurantService.getAllActiveRestaurants();
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

  
    @PutMapping("/update/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable Long id,
                                                       @Valid @RequestBody RestaurantDTO dto) {
        Restaurant restaurant = restaurantService.updateRestaurant(id, dto);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

  
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
        return new ResponseEntity<>("Restaurant deleted successfully!", HttpStatus.OK);
    }

 
    @GetMapping("/search")
    public ResponseEntity<List<Restaurant>> searchRestaurants(@RequestParam String keyword) {
        List<Restaurant> restaurants = restaurantService.searchRestaurants(keyword);
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }
}