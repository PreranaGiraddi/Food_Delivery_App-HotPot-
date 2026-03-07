package com.wipro.hotpot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.wipro.hotpot.dto.RestaurantDTO;
import com.wipro.hotpot.entity.Restaurant;
import com.wipro.hotpot.service.IRestaurantService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/restaurant")
@CrossOrigin(origins = "*")
public class RestaurantController {

    @Autowired
    private IRestaurantService restaurantService;

    // ✅ Add restaurant — userId as RequestParam
    @PostMapping("/add")
    public ResponseEntity<Restaurant> addRestaurant(
            @RequestParam Long userId,
            @Valid @RequestBody RestaurantDTO dto) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(restaurantService.addRestaurant(userId, dto));
    }

    // ✅ Update restaurant
    @PutMapping("/update/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(
            @PathVariable Long id,
            @Valid @RequestBody RestaurantDTO dto) {
        return ResponseEntity.ok(restaurantService.updateRestaurant(id, dto));
    }

    // ✅ Delete restaurant
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.ok("Restaurant deleted successfully!");
    }

    // ✅ Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getRestaurantById(id));
    }

    // ✅ Get all
    @GetMapping("/all")
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        return ResponseEntity.ok(restaurantService.getAllRestaurants());
    }

    // ✅ Get active only — used by user frontend
    @GetMapping("/active")
    public ResponseEntity<List<Restaurant>> getActiveRestaurants() {
        return ResponseEntity.ok(restaurantService.getActiveRestaurants());
    }

    // ✅ Search
    @GetMapping("/search")
    public ResponseEntity<List<Restaurant>> searchRestaurants(
            @RequestParam String keyword) {
        return ResponseEntity.ok(restaurantService.searchRestaurants(keyword));
    }

    // ✅ Toggle active/inactive — Fix: no dto needed!
    @PutMapping("/toggle/{id}")
    public ResponseEntity<Restaurant> toggleStatus(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.toggleRestaurantStatus(id));
    }

    // ✅ Get restaurant by owner userId — used by restaurant dashboard
    @GetMapping("/owner/{userId}")
    public ResponseEntity<?> getByOwner(@PathVariable Long userId) {
        try {
            Restaurant restaurant = restaurantService.getRestaurantByOwnerId(userId);
            return ResponseEntity.ok(restaurant);
        } catch (Exception e) {
            return ResponseEntity.status(404)
                .body("No restaurant found for this owner!");
        }
    }
}