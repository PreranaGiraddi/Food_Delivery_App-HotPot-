package com.wipro.hotpot.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.wipro.hotpot.dto.MenuDTO;
import com.wipro.hotpot.entity.Category;
import com.wipro.hotpot.entity.MenuItem;
import com.wipro.hotpot.entity.Restaurant;
import com.wipro.hotpot.repository.ICategoryRepository;
import com.wipro.hotpot.repository.IMenuRepository;
import com.wipro.hotpot.repository.IRestaurantRepository;
import com.wipro.hotpot.service.MenuServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IMenuServiceTest {

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

        // Sample restaurant
        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("HotPot Restaurant");

        // Sample category
        category = new Category();
        category.setId(1L);
        category.setName("Burger");
        category.setRestaurant(restaurant);

        // Sample menuDTO (what user sends)
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

        // Sample saved menuItem
        menuItem = new MenuItem();
        menuItem.setId(1L);
        menuItem.setName("Chicken Burger");
        menuItem.setPrice(199.0);
        menuItem.setAvailable(true);
        menuItem.setDietaryType(MenuItem.DietaryType.NONVEG);
        menuItem.setCategory(category);
        menuItem.setRestaurant(restaurant);
    }

    //  TEST 1 — Add menu item successfully
    @Test
    public void testAddMenuItem_Success() {

        // ARRANGE
        when(restaurantRepository.findById(1L))
                .thenReturn(Optional.of(restaurant));
        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));
        when(menuRepository.save(any(MenuItem.class)))
                .thenReturn(menuItem);

        // ACT
        MenuItem result = menuService.addMenuItem(menuDTO);

        // ASSERT
        assertNotNull(result);
        assertEquals("Chicken Burger", result.getName());
        assertEquals(199.0, result.getPrice());
        assertEquals(MenuItem.DietaryType.NONVEG, result.getDietaryType());
        assertTrue(result.isAvailable());

        verify(menuRepository, times(1)).save(any(MenuItem.class));

        System.out.println("Add Menu Item Test Passed!");
    }

    //  TEST 2 — Add menu item fails if restaurant not found
    @Test
    public void testAddMenuItem_RestaurantNotFound() {

        // ARRANGE
        when(restaurantRepository.findById(1L))
                .thenReturn(Optional.empty());

        // ACT + ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            menuService.addMenuItem(menuDTO);
        });

        assertEquals("Restaurant not found!", exception.getMessage());
        verify(menuRepository, never()).save(any(MenuItem.class));

        System.out.println("Restaurant Not Found Test Passed!");
    }

    //  TEST 3 — Get all menu items by restaurant
    @Test
    public void testGetAllMenuItemsByRestaurant() {

        // ARRANGE
        List<MenuItem> menuItems = Arrays.asList(menuItem);
        when(menuRepository.findByRestaurantId(1L))
                .thenReturn(menuItems);

        // ACT
        List<MenuItem> result = menuService.getAllMenuItemsByRestaurant(1L);

        // ASSERT
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Chicken Burger", result.get(0).getName());

        System.out.println("Get Menu Items By Restaurant Test Passed!");
    }

    // TEST 4 — Mark menu item as out of stock
    @Test
    public void testMarkOutOfStock() {

        // ARRANGE
        when(menuRepository.findById(1L))
                .thenReturn(Optional.of(menuItem));
        when(menuRepository.save(any(MenuItem.class)))
                .thenReturn(menuItem);

        // ACT
        menuService.markOutOfStock(1L);

        // ASSERT
        assertFalse(menuItem.isAvailable()); // should be false now

        verify(menuRepository, times(1)).save(menuItem);

        System.out.println("✅ Mark Out Of Stock Test Passed!");
    }
}
