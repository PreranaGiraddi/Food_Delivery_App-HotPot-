package com.wipro.hotpot.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

        // Sample user
        user = new User();
        user.setId(1L);
        user.setName("John");
        user.setEmail("john@gmail.com");

        // Sample cart
        cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setTotalPrice(0.0);

        // Sample menu item
        menuItem = new MenuItem();
        menuItem.setId(1L);
        menuItem.setName("Chicken Burger");
        menuItem.setPrice(199.0);
        menuItem.setAvailable(true);

        // Sample cart item
        cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setCart(cart);
        cartItem.setMenuItem(menuItem);
        cartItem.setQuantity(2);
        cartItem.setTotalItemPrice(398.0);
    }

    // ✅ TEST 1 — Get existing cart by user id
    @Test
    public void testGetCartByUserId_CartExists() {

        // ARRANGE
        when(cartRepository.isCartExists(1L)).thenReturn(true);
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));

        // ACT
        Cart result = cartService.getCartByUserId(1L);

        // ASSERT
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getUser().getId());

        System.out.println("✅ Get Cart By User Id Test Passed!");
    }

    // ✅ TEST 2 — Create new cart if not exists
    @Test
    public void testGetCartByUserId_CartNotExists() {

        // ARRANGE
        when(cartRepository.isCartExists(1L)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));

        // ACT
        Cart result = cartService.getCartByUserId(1L);

        // ASSERT
        assertNotNull(result);
        verify(cartRepository, times(1)).save(any(Cart.class)); // new cart created

        System.out.println("✅ Create New Cart Test Passed!");
    }

    // ✅ TEST 3 — Add item to cart
    @Test
    public void testAddItemToCart_Success() {

        // ARRANGE
        when(cartRepository.isCartExists(1L)).thenReturn(true);
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menuItem));
        when(cartItemRepository.findByCartIdAndMenuItemId(1L, 1L))
                .thenReturn(Optional.empty()); // item not in cart yet
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        // ACT
        Cart result = cartService.addItemToCart(1L, 1L, 2);

        // ASSERT
        assertNotNull(result);
        verify(cartItemRepository, times(1)).save(any(CartItem.class));

        System.out.println("✅ Add Item To Cart Test Passed!");
    }

    // ✅ TEST 4 — Clear cart
    @Test
    public void testClearCart() {

        // ARRANGE
        when(cartRepository.isCartExists(1L)).thenReturn(true);
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        // ACT
        cartService.clearCart(1L);

        // ASSERT
        verify(cartItemRepository, times(1)).deleteByCartId(1L);
        assertEquals(0.0, cart.getTotalPrice());

        System.out.println("✅ Clear Cart Test Passed!");
    }
}