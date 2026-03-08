package com.wipro.hotpot.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wipro.hotpot.dto.RestaurantDTO;
import com.wipro.hotpot.service.IRestaurantService;

@RestController
@RequestMapping("/api/restaurant")
@CrossOrigin(origins = "*")
public class RestaurantController {

    @Autowired
    private IRestaurantService restaurantService;

    // POST /api/restaurant/add?userId=1
    // Body: { "name", "location", "contactNumber", "description", "imageUrl" }
    @PostMapping("/add")
    public ResponseEntity<?> addRestaurant(@RequestBody RestaurantDTO dto,
                                            @RequestParam Long userId) {
        try {
            return ResponseEntity.ok(restaurantService.addRestaurant(dto, userId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // PUT /api/restaurant/update/{id}
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRestaurant(@PathVariable Long id,
                                               @RequestBody RestaurantDTO dto) {
        try {
            return ResponseEntity.ok(restaurantService.updateRestaurant(id, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // GET /api/restaurant/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(restaurantService.getRestaurantById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // GET /api/restaurant/owner/{userId}  ← dashboard pages use this
    @GetMapping("/owner/{userId}")
    public ResponseEntity<?> getByOwner(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(restaurantService.getRestaurantByOwnerId(userId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }

    // GET /api/restaurant/all
    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(restaurantService.getAllRestaurants());
    }

    // GET /api/restaurant/active
    @GetMapping("/active")
    public ResponseEntity<?> getActive() {
        return ResponseEntity.ok(restaurantService.getActiveRestaurants());
    }

    // PUT /api/restaurant/toggle/{id}?active=true/false
    @PutMapping("/toggle/{id}")
    public ResponseEntity<?> toggleStatus(@PathVariable Long id,
                                           @RequestParam boolean active) {
        try {
            return ResponseEntity.ok(restaurantService.toggleActive(id, active));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // DELETE /api/restaurant/delete/{id}
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            restaurantService.deleteRestaurant(id);
            return ResponseEntity.ok(Map.of("message", "Restaurant deleted!"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}