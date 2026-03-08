package com.wipro.hotpot.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wipro.hotpot.entity.Cart;
import com.wipro.hotpot.entity.CartItem;
import com.wipro.hotpot.entity.MenuItem;
import com.wipro.hotpot.entity.User;
import com.wipro.hotpot.repository.ICartItemRepository;
import com.wipro.hotpot.repository.ICartRepository;
import com.wipro.hotpot.repository.IMenuRepository;
import com.wipro.hotpot.repository.IUserRepository;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    

    @Mock
    private ICartRepository cartRepository;

    @Mock
    private ICartItemRepository cartItemRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IMenuRepository menuRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    

    private User user;
    private Cart cart;
    private MenuItem menuItem;
    private CartItem cartItem;

    @BeforeEach
    public void setUp() {

        user = new User();
        user.setId(1L);
        user.setName("Thushara S");
        user.setEmail("thusharasatheesh1@gmail.com");

        cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setTotalPrice(0.0);

        menuItem = new MenuItem();
        menuItem.setId(1L);
        menuItem.setName("Spicy Chicken Wings");
        menuItem.setPrice(299.0);
        menuItem.setAvailable(true);

        cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setCart(cart);
        cartItem.setMenuItem(menuItem);
        cartItem.setQuantity(2);
        cartItem.setTotalItemPrice(598.0); 
    }

    
    @Test
    public void testGetCartByUserId_CartExists() {
        when(cartRepository.isCartExists(1L)).thenReturn(true);
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));

        Cart result = cartService.getCartByUserId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getUser().getId());

        
        verify(cartRepository, never()).save(any(Cart.class));
        System.out.println("testGetCartByUserId_CartExists passed");
    }

    
    @Test
    public void testGetCartByUserId_CartNotExists_CreatesNew() {
        when(cartRepository.isCartExists(1L)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));

        Cart result = cartService.getCartByUserId(1L);

        assertNotNull(result);
        
        verify(cartRepository, times(1)).save(any(Cart.class));
        System.out.println("testGetCartByUserId_CartNotExists_CreatesNew passed");
    }

   
    @Test
    public void testGetCartByUserId_UserNotFound() {
        when(cartRepository.isCartExists(99L)).thenReturn(false);
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> cartService.getCartByUserId(99L));
        System.out.println("✅ testGetCartByUserId_UserNotFound passed");
    }

    
    @Test
    public void testAddItemToCart_NewItem() {
        when(cartRepository.isCartExists(1L)).thenReturn(true);
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menuItem));
        // Item not in cart yet
        when(cartItemRepository.findByCartIdAndMenuItemId(1L, 1L))
                .thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart result = cartService.addItemToCart(1L, 1L, 2);

        assertNotNull(result);
        // New CartItem must be saved
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
        // Cart total must be saved after update
        verify(cartRepository, times(1)).save(any(Cart.class));
        System.out.println("✅ testAddItemToCart_NewItem passed");
    }

    
    @Test
    public void testAddItemToCart_ExistingItem_IncreasesQuantity() {
        cartItem.setQuantity(1);
        cartItem.setTotalItemPrice(299.0);

        when(cartRepository.isCartExists(1L)).thenReturn(true);
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menuItem));
        // Item already exists in cart
        when(cartItemRepository.findByCartIdAndMenuItemId(1L, 1L))
                .thenReturn(Optional.of(cartItem));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart result = cartService.addItemToCart(1L, 1L, 2);

        assertNotNull(result);
        
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
        
        assertEquals(3, cartItem.getQuantity());
        System.out.println("✅ testAddItemToCart_ExistingItem_IncreasesQuantity passed");
    }

    
    @Test
    public void testAddItemToCart_MenuItemNotFound() {
        when(cartRepository.isCartExists(1L)).thenReturn(true);
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(menuRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
            cartService.addItemToCart(1L, 99L, 1)
        );

        verify(cartItemRepository, never()).save(any(CartItem.class));
        System.out.println("testAddItemToCart_MenuItemNotFound passed");
    }

    
    @Test
    public void testAddItemToCart_MenuItemUnavailable() {
        menuItem.setAvailable(false);

        when(cartRepository.isCartExists(1L)).thenReturn(true);
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menuItem));

        assertThrows(RuntimeException.class, () ->
            cartService.addItemToCart(1L, 1L, 1)
        );

        verify(cartItemRepository, never()).save(any(CartItem.class));
        System.out.println("testAddItemToCart_MenuItemUnavailable passed");
    }

   
    @Test
    public void testRemoveItemFromCart_Success() {
        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(cartItem));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        assertDoesNotThrow(() -> cartService.removeItemFromCart(1L, 1L));

        verify(cartItemRepository, times(1)).delete(cartItem);
        System.out.println(" testRemoveItemFromCart_Success passed");
    }

    
    @Test
    public void testRemoveItemFromCart_NotFound() {
        when(cartItemRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
            cartService.removeItemFromCart(1L, 99L)
        );

        verify(cartItemRepository, never()).delete(any(CartItem.class));
        System.out.println("testRemoveItemFromCart_NotFound passed");
    }

    
    @Test
    public void testClearCart_Success() {
        when(cartRepository.isCartExists(1L)).thenReturn(true);
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        cartService.clearCart(1L);

        verify(cartItemRepository, times(1)).deleteByCartId(1L);
        assertEquals(0.0, cart.getTotalPrice());
        System.out.println("testClearCart_Success passed");
    }

    
    @Test
    public void testGetCartItems_ReturnsList() {
        CartItem cartItem2 = new CartItem();
        cartItem2.setId(2L);
        cartItem2.setCart(cart);
        cartItem2.setQuantity(1);
        cartItem2.setTotalItemPrice(299.0);

        when(cartRepository.isCartExists(1L)).thenReturn(true);
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartId(1L))
                .thenReturn(Arrays.asList(cartItem, cartItem2));

        List<CartItem> result = cartService.getCartItems(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        System.out.println("testGetCartItems_ReturnsList passed");
    }

    
    @Test
    public void testUpdateItemQuantity_Success() {
        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(cartItem));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        cartService.updateItemQuantity(1L, 1L, 3);

        assertEquals(3, cartItem.getQuantity());
        assertEquals(897.0, cartItem.getTotalItemPrice(), 0.01);
        verify(cartItemRepository, times(1)).save(cartItem);
        System.out.println("testUpdateItemQuantity_Success passed");
    }

    
    @Test
    public void testUpdateItemQuantity_ToZero_RemovesItem() {
        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(cartItem));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        cartService.updateItemQuantity(1L, 1L, 0);

        verify(cartItemRepository, times(1)).delete(cartItem);
        System.out.println("testUpdateItemQuantity_ToZero_RemovesItem passed");
    }
}