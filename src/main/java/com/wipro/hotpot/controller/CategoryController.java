package com.wipro.hotpot.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import com.wipro.hotpot.dto.CategoryDTO;
import com.wipro.hotpot.entity.Category;
import com.wipro.hotpot.entity.Restaurant;
import com.wipro.hotpot.exception.ResourceNotFoundException;
import com.wipro.hotpot.repository.ICategoryRepository;
import com.wipro.hotpot.repository.IRestaurantRepository;

@RestController
@RequestMapping("/api/category")
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    private ICategoryRepository categoryRepository;

    @Autowired
    private IRestaurantRepository restaurantRepository;

    // GET all categories for a restaurant
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Category>> getByRestaurant(
            @PathVariable Long restaurantId) {
        List<Category> list = categoryRepository.findByRestaurantId(restaurantId);
        return ResponseEntity.ok(list);
    }

    // GET all categories (admin / general use)
    @GetMapping("/all")
    public ResponseEntity<List<Category>> getAll() {
        return ResponseEntity.ok(categoryRepository.findAll());
    }

    // GET single category by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Category cat = categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return ResponseEntity.ok(cat);
    }

    // ADD new category
    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@RequestBody CategoryDTO dto) {

        // Validate name
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Category name is required!");
        }

        // Validate restaurantId
        if (dto.getRestaurantId() == null) {
            return ResponseEntity.badRequest().body("Restaurant ID is required!");
        }

        // Check duplicate
        if (categoryRepository.existsByNameAndRestaurantId(
                dto.getName().trim(), dto.getRestaurantId())) {
            return ResponseEntity.badRequest()
                .body("Category '" + dto.getName() + "' already exists for this restaurant!");
        }

        // Find restaurant
        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Restaurant not found with id: " + dto.getRestaurantId()));

        // Save
        Category cat = new Category();
        cat.setName(dto.getName().trim());
        cat.setRestaurant(restaurant);

        Category saved = categoryRepository.save(cat);
        return ResponseEntity.ok(saved);
    }

    // UPDATE category name
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryDTO dto) {

        Category cat = categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        if (dto.getName() != null && !dto.getName().trim().isEmpty()) {
            cat.setName(dto.getName().trim());
        }

        return ResponseEntity.ok(categoryRepository.save(cat));
    }

    //  DELETE category
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        if (!categoryRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
        return ResponseEntity.ok("Category deleted successfully!");
    }
}
