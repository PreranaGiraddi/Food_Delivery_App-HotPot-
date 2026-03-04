package com.wipro.hotpot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.hotpot.dto.MenuDTO;
import com.wipro.hotpot.entity.Category;
import com.wipro.hotpot.entity.MenuItem;
import com.wipro.hotpot.entity.Restaurant;
import com.wipro.hotpot.exception.ResourceNotFoundException;
import com.wipro.hotpot.repository.ICategoryRepository;
import com.wipro.hotpot.repository.IMenuRepository;
import com.wipro.hotpot.repository.IRestaurantRepository;

@Service
public class MenuServiceImpl implements IMenuService {

	@Autowired
	private IMenuRepository menuRepository;

	@Autowired
	private IRestaurantRepository restaurantRepository;

	@Autowired
	private ICategoryRepository categoryRepository;

	
	@Override
	public MenuItem addMenuItem(MenuDTO dto) {

		Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
				.orElseThrow(() -> new ResourceNotFoundException("Restaurant not found!"));

		Category category = categoryRepository.findById(dto.getCategoryId())
				.orElseThrow(() -> new ResourceNotFoundException("Category not found!"));

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

	
	@Override
	public MenuItem getMenuItemById(Long id) {
		return menuRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));

	}

	
	@Override
	public List<MenuItem> getAllMenuItemsByRestaurant(Long restaurantId) {
		return menuRepository.findByRestaurantId(restaurantId);
	}

	
	@Override
	public List<MenuItem> getAvailableMenuItems(Long restaurantId) {
		return menuRepository.findByRestaurantIdAndIsAvailable(restaurantId, true);
	}

	
	@Override
	public List<MenuItem> getMenuItemsByCategory(Long categoryId) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new RuntimeException("Category not found!"));
		return menuRepository.findByCategory(category);
	}

	
	@Override
	public List<MenuItem> filterByDietaryType(Long restaurantId, String dietaryType) {
		return menuRepository.findByRestaurantIdAndDietaryType(restaurantId, MenuItem.DietaryType.valueOf(dietaryType));
	}

	
	@Override
	public List<MenuItem> searchMenuItems(String keyword) {
		return menuRepository.globalSearch(keyword);
	}


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

	
	@Override
	public void deleteMenuItem(Long id) {
		menuRepository.deleteById(id);
	}

	
	@Override
	public void markOutOfStock(Long id) {
		MenuItem item = getMenuItemById(id);
		item.setAvailable(false);
		menuRepository.save(item);
	}
}
