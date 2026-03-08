package com.wipro.hotpot.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wipro.hotpot.dto.MenuDTO;
import com.wipro.hotpot.entity.Category;
import com.wipro.hotpot.entity.MenuItem;
import com.wipro.hotpot.entity.Restaurant;

import com.wipro.hotpot.repository.ICategoryRepository;
import com.wipro.hotpot.repository.IMenuRepository;
import com.wipro.hotpot.repository.IRestaurantRepository;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    

    @Mock
    private IMenuRepository menuRepository;

    @Mock
    private IRestaurantRepository restaurantRepository;

    @Mock
    private ICategoryRepository categoryRepository;

    @InjectMocks
    private MenuServiceImpl menuService;

    

    private MenuDTO menuDTO;
    private MenuItem menuItem;
    private Restaurant restaurant;
    private Category category;

    @BeforeEach
    public void setUp() {

        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("HotPot Restaurant");

        category = new Category();
        category.setId(1L);
        category.setName("Burger");
        category.setRestaurant(restaurant);

       
        menuDTO = new MenuDTO();
        menuDTO.setName("Chicken Burger");
        menuDTO.setDescription("Juicy chicken burger");
        menuDTO.setPrice(199.0);
        menuDTO.setDiscountPrice(149.0);
        menuDTO.setDietaryType("NONVEG");
        menuDTO.setAvailabilityTime("all day");
        menuDTO.setTasteInfo("spicy full");
        menuDTO.setCookingTime(15);
        menuDTO.setCategoryId(1L);
        menuDTO.setRestaurantId(1L);
        menuDTO.setAvailable(true);

       
        menuItem = new MenuItem();
        menuItem.setId(1L);
        menuItem.setName("Chicken Burger");
        menuItem.setPrice(199.0);
        menuItem.setAvailable(true);           
        menuItem.setDietaryType(MenuItem.DietaryType.NONVEG);
        menuItem.setCategory(category);
        menuItem.setRestaurant(restaurant);
    }

   
    @Test
    public void testAddMenuItem_Success() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(menuRepository.save(any(MenuItem.class))).thenReturn(menuItem);

        MenuItem result = menuService.addMenuItem(menuDTO);

        assertNotNull(result);
        assertEquals("Chicken Burger", result.getName());
        assertEquals(199.0, result.getPrice());
        assertEquals(MenuItem.DietaryType.NONVEG, result.getDietaryType());
        assertTrue(result.getIsAvailable());   
        verify(menuRepository, times(1)).save(any(MenuItem.class));
        System.out.println("testAddMenuItem_Success passed");
    }

   
    @Test
    public void testAddMenuItem_RestaurantNotFound() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
            menuService.addMenuItem(menuDTO)
        );

        assertTrue(ex.getMessage().contains("Restaurant not found"),
            "Expected 'Restaurant not found' but got: " + ex.getMessage());
        verify(menuRepository, never()).save(any(MenuItem.class));
        System.out.println("testAddMenuItem_RestaurantNotFound passed");
    }

   
    @Test
    public void testAddMenuItem_CategoryNotFound() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
            menuService.addMenuItem(menuDTO)
        );

        assertTrue(ex.getMessage().contains("Category not found"),
            "Expected 'Category not found' but got: " + ex.getMessage());
        verify(menuRepository, never()).save(any(MenuItem.class));
        System.out.println(" testAddMenuItem_CategoryNotFound passed");
    }

   

    @Test
    public void testGetAllMenuItemsByRestaurant_ReturnsList() {
        MenuItem menuItem2 = new MenuItem();
        menuItem2.setId(2L);
        menuItem2.setName("Veg Pizza");
        menuItem2.setPrice(149.0);
        menuItem2.setAvailable(true);
        menuItem2.setDietaryType(MenuItem.DietaryType.VEG);
        menuItem2.setRestaurant(restaurant);

        when(menuRepository.findByRestaurantId(1L))
                .thenReturn(Arrays.asList(menuItem, menuItem2));

        List<MenuItem> result = menuService.getAllMenuItemsByRestaurant(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Chicken Burger", result.get(0).getName());
        assertEquals("Veg Pizza", result.get(1).getName());
        System.out.println("testGetAllMenuItemsByRestaurant_ReturnsList passed");
    }

    @Test
    public void testGetAllMenuItemsByRestaurant_EmptyList() {
        when(menuRepository.findByRestaurantId(99L)).thenReturn(List.of());

        List<MenuItem> result = menuService.getAllMenuItemsByRestaurant(99L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        System.out.println("testGetAllMenuItemsByRestaurant_EmptyList passed");
    }

    

    @Test
    public void testGetMenuItemById_Success() {
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menuItem));

        MenuItem result = menuService.getMenuItemById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Chicken Burger", result.getName());
        System.out.println("testGetMenuItemById_Success passed");
    }

    @Test
    public void testGetMenuItemById_NotFound() {
        when(menuRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> menuService.getMenuItemById(99L));
        System.out.println("testGetMenuItemById_NotFound passed");
    }

    
    @Test
    public void testUpdateMenuItem_Success() {
        menuDTO.setName("Grilled Chicken Burger");
        menuDTO.setPrice(219.0);

        MenuItem updatedItem = new MenuItem();
        updatedItem.setId(1L);
        updatedItem.setName("Grilled Chicken Burger");
        updatedItem.setPrice(219.0);
        updatedItem.setAvailable(true);
        updatedItem.setRestaurant(restaurant);

        when(menuRepository.findById(1L)).thenReturn(Optional.of(menuItem));
        when(menuRepository.save(any(MenuItem.class))).thenReturn(updatedItem);

        MenuItem result = menuService.updateMenuItem(1L, menuDTO);

        assertNotNull(result);
        assertEquals("Grilled Chicken Burger", result.getName());
        assertEquals(219.0, result.getPrice());
        verify(menuRepository, times(1)).save(any(MenuItem.class));
        System.out.println("testUpdateMenuItem_Success passed");
    }

    
    @Test
    public void testMarkOutOfStock_SetsAvailableFalse() {
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menuItem));
        when(menuRepository.save(any(MenuItem.class))).thenReturn(menuItem);

        menuService.markOutOfStock(1L);

        assertFalse(menuItem.getIsAvailable());   
        verify(menuRepository, times(1)).save(menuItem);
        System.out.println("testMarkOutOfStock_SetsAvailableFalse passed");
    }

   
    @Test
    public void testToggleAvailability_SetTrue() {
        menuItem.setAvailable(false); 

        when(menuRepository.findById(1L)).thenReturn(Optional.of(menuItem));
        when(menuRepository.save(any(MenuItem.class))).thenReturn(menuItem);

        menuService.toggleAvailability(1L, true);

        assertTrue(menuItem.getIsAvailable());
        verify(menuRepository, times(1)).save(menuItem);
        System.out.println("testToggleAvailability_SetTrue passed");
    }

    

    @Test
    public void testDeleteMenuItem_Success() {
        when(menuRepository.existsById(1L)).thenReturn(true);
        doNothing().when(menuRepository).deleteById(1L);

        assertDoesNotThrow(() -> menuService.deleteMenuItem(1L));
        verify(menuRepository, times(1)).deleteById(1L);
        System.out.println("testDeleteMenuItem_Success passed");
    }

    @Test
    public void testDeleteMenuItem_NotFound() {
        when(menuRepository.existsById(99L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> menuService.deleteMenuItem(99L));
        verify(menuRepository, never()).deleteById(any());
        System.out.println("testDeleteMenuItem_NotFound passed");
    }
}