package com.hotpot.service.menu;

import com.hotpot.dto.menu.MenuDTO;
import com.hotpot.entity.menu.Category;
import com.hotpot.entity.menu.MenuItem;
import com.hotpot.entity.menu.Restaurant;
import com.hotpot.repository.menu.CategoryRepository;
import com.hotpot.repository.menu.MenuRepository;
import com.hotpot.repository.menu.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // ✅ Add menu item
    @Override
    public MenuItem addMenuItem(MenuDTO dto) {

        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found!"));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found!"));

        MenuItem item = new MenuItem();
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setPrice(dto.getPrice());
        item.setDiscountPrice(dto.getDiscountPrice());
        item.setImageUrl(dto.getImageUrl());
        item.setAvailabilityTime(dto.getAvailabilityTime());
        item.setDietaryType(MenuItem.DietaryType.valueOf(dto.getDietaryType()));
        item.setTasteInfo(dto.getTasteInfo());
        item.setNutritionalInfo(dto.getNutritionalInfo());
        item.setCookingTime(dto.getCookingTime());
        item.setAvailable(true);
        item.setCategory(category);
        item.setRestaurant(restaurant);

        return menuRepository.save(item);
    }

    // ✅ Get menu item by id
    @Override
    public MenuItem getMenuItemById(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found with id: " + id));
    }

    // ✅ Get all menu items by restaurant
    @Override
    public List<MenuItem> getAllMenuItemsByRestaurant(Long restaurantId) {
        return menuRepository.findByRestaurantId(restaurantId);
    }

    // ✅ Get available menu items
    @Override
    public List<MenuItem> getAvailableMenuItems(Long restaurantId) {
        return menuRepository.findByRestaurantIdAndIsAvailable(restaurantId, true);
    }

    // ✅ Get menu items by category
    @Override
    public List<MenuItem> getMenuItemsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found!"));
        return menuRepository.findByCategory(category);
    }

    // ✅ Filter by dietary type
    @Override
    public List<MenuItem> filterByDietaryType(Long restaurantId, String dietaryType) {
        return menuRepository.findByRestaurantIdAndDietaryType(
                restaurantId, MenuItem.DietaryType.valueOf(dietaryType));
    }

    // ✅ Search menu items
    @Override
    public List<MenuItem> searchMenuItems(String keyword) {
        return menuRepository.globalSearch(keyword);
    }

    // ✅ Update menu item
    @Override
    public MenuItem updateMenuItem(Long id, MenuDTO dto) {
        MenuItem item = getMenuItemById(id);
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setPrice(dto.getPrice());
        item.setDiscountPrice(dto.getDiscountPrice());
        item.setImageUrl(dto.getImageUrl());
        item.setAvailabilityTime(dto.getAvailabilityTime());
        item.setTasteInfo(dto.getTasteInfo());
        item.setNutritionalInfo(dto.getNutritionalInfo());
        item.setCookingTime(dto.getCookingTime());
        item.setAvailable(dto.isAvailable());
        return menuRepository.save(item);
    }

    // ✅ Delete menu item
    @Override
    public void deleteMenuItem(Long id) {
        menuRepository.deleteById(id);
    }

    // ✅ Mark out of stock
    @Override
    public void markOutOfStock(Long id) {
        MenuItem item = getMenuItemById(id);
        item.setAvailable(false);
        menuRepository.save(item);
    }
}

