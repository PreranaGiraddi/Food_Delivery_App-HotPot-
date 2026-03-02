package com.wipro.hotpot.controller;

import com.wipro.hotpot.dto.MenuDTO;
import com.wipro.hotpot.entity.MenuItem;
import com.wipro.hotpot.service.IMenuService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    @Autowired
    private IMenuService menuService;

    //Add new menu item
    @PostMapping("/add")
    public ResponseEntity<MenuItem> addMenuItem(@Valid @RequestBody MenuDTO dto) {
        MenuItem item = menuService.addMenuItem(dto);
        return new ResponseEntity<>(item, HttpStatus.CREATED);
    }

    //Get menu item by id
    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> getMenuItemById(@PathVariable Long id) {
        MenuItem item = menuService.getMenuItemById(id);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    // Get all menu items by restaurant
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<MenuItem>> getAllMenuItemsByRestaurant(
            @PathVariable Long restaurantId) {
        List<MenuItem> items = menuService.getAllMenuItemsByRestaurant(restaurantId);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    //Get available menu items
    @GetMapping("/available/{restaurantId}")
    public ResponseEntity<List<MenuItem>> getAvailableMenuItems(
            @PathVariable Long restaurantId) {
        List<MenuItem> items = menuService.getAvailableMenuItems(restaurantId);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    // Get menu items by category
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<MenuItem>> getMenuItemsByCategory(
            @PathVariable Long categoryId) {
        List<MenuItem> items = menuService.getMenuItemsByCategory(categoryId);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    // Filter menu items by dietary type
    @GetMapping("/filter")
    public ResponseEntity<List<MenuItem>> filterByDietaryType(
            @RequestParam Long restaurantId,
            @RequestParam String dietaryType) {
        List<MenuItem> items = menuService.filterByDietaryType(restaurantId, dietaryType);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    //  Search menu items globally
    @GetMapping("/search")
    public ResponseEntity<List<MenuItem>> searchMenuItems(@RequestParam String keyword) {
        List<MenuItem> items = menuService.searchMenuItems(keyword);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    //  Update menu item
    @PutMapping("/update/{id}")
    public ResponseEntity<MenuItem> updateMenuItem(@PathVariable Long id,
                                                   @Valid @RequestBody MenuDTO dto) {
        MenuItem item = menuService.updateMenuItem(id, dto);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    // Delete menu item
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteMenuItem(@PathVariable Long id) {
        menuService.deleteMenuItem(id);
        return new ResponseEntity<>("Menu item deleted successfully!", HttpStatus.OK);
    }

    //  Mark menu item as out of stock
    @PutMapping("/outofstock/{id}")
    public ResponseEntity<String> markOutOfStock(@PathVariable Long id) {
        menuService.markOutOfStock(id);
        return new ResponseEntity<>("Menu item marked as out of stock!", HttpStatus.OK);
    }
}
