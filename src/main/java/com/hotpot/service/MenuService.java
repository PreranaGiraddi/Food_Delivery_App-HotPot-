package com.hotpot.service;

import java.util.List;

import com.hotpot.dto.MenuDTO;
import com.hotpot.entity.MenuItem;

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
