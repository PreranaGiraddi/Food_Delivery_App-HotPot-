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
        restaurant.setId(2L);                                   // Meghna Biriyani
        restaurant.setName("Meghna Biriyani");
        restaurant.setLocation("Banglore");
        restaurant.setContactNumber("9876543211");

        category = new Category();
        category.setId(16L);                                    // Biriyani's category
        category.setName("Biriyani's");
        category.setRestaurant(restaurant);

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

        menuItem = new MenuItem();
        menuItem.setId(24L);                                    // matches DB id=24
        menuItem.setName("Panner Biriyani");
        menuItem.setPrice(250.0);
        menuItem.setDiscountPrice(199.0);
        menuItem.setAvailable(true);
        menuItem.setDietaryType(MenuItem.DietaryType.VEG);
        menuItem.setAvailabilityTime("Lunch");
        menuItem.setCategory(category);
        menuItem.setRestaurant(restaurant);
    }

    // ---------------------------------------------------------------
    // TEST 1: Add menu item successfully
    // DB ref: Panner Biriyani in Meghna Biriyani (restaurant_id=2)
    // ---------------------------------------------------------------
    @Test
    public void testAddMenuItem_Success() {

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
    }

    // ---------------------------------------------------------------
    // TEST 2: Add menu item fails — restaurant not found
    // Using restaurant_id=99 which does not exist in DB
    // ---------------------------------------------------------------
    @Test
    public void testAddMenuItem_RestaurantNotFound() {

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
    @Test
    public void testGetAllMenuItemsByRestaurant() {

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
    }

    // ---------------------------------------------------------------
    // TEST 5: Mark a menu item as out of stock
    // DB ref: id=16 "Chicken Dum Biriyani" is_available=0x00 (already out of stock)
    //         Using id=24 "Panner Biriyani" which is currently available (0x01)
    // ---------------------------------------------------------------
    @Test
    public void testMarkOutOfStock() {

        when(menuRepository.findById(24L))
                .thenReturn(Optional.of(menuItem));
        when(menuRepository.save(any(MenuItem.class)))
                .thenReturn(menuItem);

        menuService.markOutOfStock(24L);

        assertFalse(menuItem.getIsAvailable());                    // must flip to false

        verify(menuRepository, times(1)).save(menuItem);

        System.out.println("✅ Mark Out Of Stock Test Passed!");
    }

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
    }
}