package com.wipro.hotpot.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
<<<<<<< HEAD

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

=======

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

>>>>>>> feature-auth-user
import com.wipro.hotpot.dto.MenuDTO;
import com.wipro.hotpot.entity.Category;
import com.wipro.hotpot.entity.MenuItem;
import com.wipro.hotpot.entity.Restaurant;
<<<<<<< HEAD

=======
>>>>>>> feature-auth-user
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
        restaurant.setId(2L);                                   // Meghna Biriyani
        restaurant.setName("Meghna Biriyani");
        restaurant.setLocation("Banglore");
        restaurant.setContactNumber("9876543211");

        category = new Category();
        category.setId(16L);                                    // Biriyani's category
        category.setName("Biriyani's");
        category.setRestaurant(restaurant);

<<<<<<< HEAD
       
=======
>>>>>>> feature-auth-user
        menuDTO = new MenuDTO();
        menuDTO.setName("Panner Biriyani");
        menuDTO.setDescription("Aromatic Panner biriyani tossed with veggies and Panner");
        menuDTO.setPrice(250.0);                                // matches DB price=250
        menuDTO.setDiscountPrice(199.0);                        // matches DB discount=199
        menuDTO.setDietaryType("VEG");
        menuDTO.setAvailabilityTime("Lunch");                   // matches DB availability_time
        menuDTO.setTasteInfo("Spicy Light");
        menuDTO.setCookingTime(20);
        menuDTO.setCategoryId(16L);                             // Biriyani's
        menuDTO.setRestaurantId(2L);                            // Meghna Biriyani
        menuDTO.setAvailable(true);

<<<<<<< HEAD
       
        menuItem = new MenuItem();
        menuItem.setId(1L);
        menuItem.setName("Chicken Burger");
        menuItem.setPrice(199.0);
        menuItem.setAvailable(true);           
        menuItem.setDietaryType(MenuItem.DietaryType.NONVEG);
=======
        menuItem = new MenuItem();
        menuItem.setId(24L);                                    // matches DB id=24
        menuItem.setName("Panner Biriyani");
        menuItem.setPrice(250.0);
        menuItem.setDiscountPrice(199.0);
        menuItem.setAvailable(true);
        menuItem.setDietaryType(MenuItem.DietaryType.VEG);
        menuItem.setAvailabilityTime("Lunch");
>>>>>>> feature-auth-user
        menuItem.setCategory(category);
        menuItem.setRestaurant(restaurant);
    }

    // ---------------------------------------------------------------
    // TEST 1: Add menu item successfully
    // DB ref: Panner Biriyani in Meghna Biriyani (restaurant_id=2)
    // ---------------------------------------------------------------
    @Test
    public void testAddMenuItem_Success() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(menuRepository.save(any(MenuItem.class))).thenReturn(menuItem);

<<<<<<< HEAD
        MenuItem result = menuService.addMenuItem(menuDTO);

        assertNotNull(result);
        assertEquals("Chicken Burger", result.getName());
        assertEquals(199.0, result.getPrice());
        assertEquals(MenuItem.DietaryType.NONVEG, result.getDietaryType());
        assertTrue(result.getIsAvailable());   
        verify(menuRepository, times(1)).save(any(MenuItem.class));
        System.out.println("testAddMenuItem_Success passed");
=======
        when(restaurantRepository.findById(2L))
                .thenReturn(Optional.of(restaurant));
        when(categoryRepository.findById(16L))
                .thenReturn(Optional.of(category));
        when(menuRepository.save(any(MenuItem.class)))
                .thenReturn(menuItem);

        MenuItem result = menuService.addMenuItem(menuDTO);

        assertNotNull(result);
        assertEquals("Panner Biriyani", result.getName());
        assertEquals(250.0, result.getPrice());
        assertEquals(199.0, result.getDiscountPrice());
        assertEquals(MenuItem.DietaryType.VEG, result.getDietaryType());
        assertEquals("Lunch", result.getAvailabilityTime());
        assertTrue(result.getIsAvailable());

        verify(menuRepository, times(1)).save(any(MenuItem.class));

        System.out.println("✅ Add Menu Item Test Passed!");
>>>>>>> feature-auth-user
    }

    // ---------------------------------------------------------------
    // TEST 2: Add menu item fails — restaurant not found
    // Using restaurant_id=99 which does not exist in DB
    // ---------------------------------------------------------------
    @Test
    public void testAddMenuItem_RestaurantNotFound() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

<<<<<<< HEAD
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
            menuService.addMenuItem(menuDTO)
        );

        assertTrue(ex.getMessage().contains("Restaurant not found"),
            "Expected 'Restaurant not found' but got: " + ex.getMessage());
        verify(menuRepository, never()).save(any(MenuItem.class));
        System.out.println("testAddMenuItem_RestaurantNotFound passed");
    }

   
=======
        when(restaurantRepository.findById(99L))
                .thenReturn(Optional.empty());

        menuDTO.setRestaurantId(99L);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            menuService.addMenuItem(menuDTO);
        });

        assertEquals("Restaurant not found!", exception.getMessage());
        verify(menuRepository, never()).save(any(MenuItem.class));

        System.out.println("✅ Restaurant Not Found Test Passed!");
    }

    // ---------------------------------------------------------------
    // TEST 3: Add menu item fails — category not found
    // Restaurant exists (id=2) but category_id=99 does not exist in DB
    // ---------------------------------------------------------------
    @Test
    public void testAddMenuItem_CategoryNotFound() {

        when(restaurantRepository.findById(2L))
                .thenReturn(Optional.of(restaurant));
        when(categoryRepository.findById(99L))
                .thenReturn(Optional.empty());

        menuDTO.setCategoryId(99L);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            menuService.addMenuItem(menuDTO);
        });

        assertEquals("Category not found!", exception.getMessage());
        verify(menuRepository, never()).save(any(MenuItem.class));

        System.out.println("✅ Category Not Found Test Passed!");
    }

    // ---------------------------------------------------------------
    // TEST 4: Get all menu items for a restaurant
    // DB ref: restaurant_id=2 (Meghna Biriyani) has items:
    //   id=16 Chicken Dum Biriyani, id=17 Mutton Biriyani,
    //   id=18 Chicken Kabab, id=19 Family Biriyani Combo,
    //   id=20 Butter Naan, id=21 Coca Cola, id=22 Double Ka Meetha, id=24 Panner Biriyani
    // ---------------------------------------------------------------
>>>>>>> feature-auth-user
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

<<<<<<< HEAD
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
=======
        MenuItem mutton = new MenuItem();
        mutton.setId(17L);
        mutton.setName("Mutton Biriyani");
        mutton.setPrice(449.0);
        mutton.setDietaryType(MenuItem.DietaryType.NONVEG);
        mutton.setRestaurant(restaurant);

        List<MenuItem> menuItems = Arrays.asList(menuItem, mutton);

        when(menuRepository.findByRestaurantId(2L))
                .thenReturn(menuItems);

        List<MenuItem> result = menuService.getAllMenuItemsByRestaurant(2L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Panner Biriyani", result.get(0).getName());
        assertEquals("Mutton Biriyani", result.get(1).getName());

        System.out.println("✅ Get Menu Items By Restaurant Test Passed!");
>>>>>>> feature-auth-user
    }

    // ---------------------------------------------------------------
    // TEST 5: Mark a menu item as out of stock
    // DB ref: id=16 "Chicken Dum Biriyani" is_available=0x00 (already out of stock)
    //         Using id=24 "Panner Biriyani" which is currently available (0x01)
    // ---------------------------------------------------------------
    @Test
    public void testUpdateMenuItem_Success() {
        menuDTO.setName("Grilled Chicken Burger");
        menuDTO.setPrice(219.0);

<<<<<<< HEAD
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
=======
        when(menuRepository.findById(24L))
                .thenReturn(Optional.of(menuItem));
        when(menuRepository.save(any(MenuItem.class)))
                .thenReturn(menuItem);

        menuService.markOutOfStock(24L);

        assertFalse(menuItem.getIsAvailable());                    // must flip to false

>>>>>>> feature-auth-user
        verify(menuRepository, times(1)).save(menuItem);
        System.out.println("testMarkOutOfStock_SetsAvailableFalse passed");
    }

<<<<<<< HEAD
   
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
=======
    // ---------------------------------------------------------------
    // TEST 6: Add a NONVEG item to HotPot Restaurant
    // DB ref: restaurant_id=1 (HotPot Restaurant), category_id=1 (Starters)
    //         Similar to id=1 "Spicy Chicken Wings", price=299, discount=249
    // ---------------------------------------------------------------
    @Test
    public void testAddNonVegMenuItem_HotPotRestaurant() {

        Restaurant hotpot = new Restaurant();
        hotpot.setId(1L);
        hotpot.setName("HotPot Restaurant");

        Category starters = new Category();
        starters.setId(1L);
        starters.setName("Starters");
        starters.setRestaurant(hotpot);

        MenuDTO nonVegDTO = new MenuDTO();
        nonVegDTO.setName("Spicy Chicken Wings");
        nonVegDTO.setDescription("Crispy fried chicken wings tossed in hot sauce");
        nonVegDTO.setPrice(299.0);
        nonVegDTO.setDiscountPrice(249.0);
        nonVegDTO.setDietaryType("NONVEG");
        nonVegDTO.setAvailabilityTime("evening");               // matches DB availability_time
        nonVegDTO.setTasteInfo("spicy");
        nonVegDTO.setCookingTime(20);
        nonVegDTO.setCategoryId(1L);
        nonVegDTO.setRestaurantId(1L);
        nonVegDTO.setAvailable(true);

        MenuItem wings = new MenuItem();
        wings.setId(1L);
        wings.setName("Spicy Chicken Wings");
        wings.setPrice(299.0);
        wings.setDietaryType(MenuItem.DietaryType.NONVEG);
        wings.setAvailable(true);
        wings.setCategory(starters);
        wings.setRestaurant(hotpot);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(hotpot));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(starters));
        when(menuRepository.save(any(MenuItem.class))).thenReturn(wings);

        MenuItem result = menuService.addMenuItem(nonVegDTO);

        assertNotNull(result);
        assertEquals("Spicy Chicken Wings", result.getName());
        assertEquals(299.0, result.getPrice());
        assertEquals(MenuItem.DietaryType.NONVEG, result.getDietaryType());

        System.out.println("✅ Add NonVeg MenuItem to HotPot Test Passed!");
>>>>>>> feature-auth-user
    }
}