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

        
        user = new User();
        user.setId(1L);
        user.setName("John");
        user.setEmail("john@gmail.com");

        
        cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setTotalPrice(0.0);

      
        menuItem = new MenuItem();
        menuItem.setId(1L);
        menuItem.setName("Chicken Burger");
        menuItem.setPrice(199.0);
        menuItem.setAvailable(true);

       
        cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setCart(cart);
        cartItem.setMenuItem(menuItem);
        cartItem.setQuantity(2);
        cartItem.setTotalItemPrice(398.0);
    }

  
    @Test
    public void testGetCartByUserId_CartExists() {

   
        when(cartRepository.isCartExists(1L)).thenReturn(true);
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));

       
        Cart result = cartService.getCartByUserId(1L);

  
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getUser().getId());

        System.out.println("✅ Get Cart By User Id Test Passed!");
    }

  
    @Test
    public void testGetCartByUserId_CartNotExists() {

       
        when(cartRepository.isCartExists(1L)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));

   
        Cart result = cartService.getCartByUserId(1L);

     
        assertNotNull(result);
        verify(cartRepository, times(1)).save(any(Cart.class)); // new cart created

        System.out.println("✅ Create New Cart Test Passed!");
    }

   
    @Test
    public void testAddItemToCart_Success() {

     
        when(cartRepository.isCartExists(1L)).thenReturn(true);
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menuItem));
        when(cartItemRepository.findByCartIdAndMenuItemId(1L, 1L))
                .thenReturn(Optional.empty()); // item not in cart yet
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

     
        Cart result = cartService.addItemToCart(1L, 1L, 2);

     
        assertNotNull(result);
        verify(cartItemRepository, times(1)).save(any(CartItem.class));

        System.out.println("✅ Add Item To Cart Test Passed!");
    }

   
    @Test
    public void testClearCart() {

 
        when(cartRepository.isCartExists(1L)).thenReturn(true);
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

       
        cartService.clearCart(1L);

      
        verify(cartItemRepository, times(1)).deleteByCartId(1L);
        assertEquals(0.0, cart.getTotalPrice());

        System.out.println("✅ Clear Cart Test Passed!");
    }
}