package com.hotpot.service.menu;

import com.hotpot.dto.menu.MenuDTO;
import com.hotpot.entity.menu.MenuItem;
import java.util.List;

public interface MenuService {

    MenuItem addMenuItem(MenuDTO dto);

    MenuItem getMenuItemById(Long id);

    List<MenuItem> getAllMenuItemsByRestaurant(Long restaurantId);

    List<MenuItem> getAvailableMenuItems(Long restaurantId);

    List<MenuItem> getMenuItemsByCategory(Long categoryId);

    List<MenuItem> filterByDietaryType(Long restaurantId, String dietaryType);

    List<MenuItem> searchMenuItems(String keyword);

    MenuItem updateMenuItem(Long id, MenuDTO dto);

    void deleteMenuItem(Long id);

    void markOutOfStock(Long id);
}

