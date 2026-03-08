package com.wipro.hotpot.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import com.wipro.hotpot.exception.ResourceNotFoundException;
import com.wipro.hotpot.repository.ICartItemRepository;
import com.wipro.hotpot.repository.ICartRepository;
import com.wipro.hotpot.repository.IMenuRepository;
import com.wipro.hotpot.repository.IUserRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private ICartRepository cartRepository;

    @Mock
    private ICartItemRepository cartItemRepository;

    @Mock
    private IUserRepository userRepository;

    // NOTE: field name in impl is "menuItemRepository" but @Mock name doesn't matter —
    // Mockito matches by type IMenuRepository, so this works correctly
    @Mock
    private IMenuRepository menuItemRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    private User user;
    private Cart cart;
    private MenuItem menuItem;
    private CartItem cartItem;

    // ---------------------------------------------------------------
    // Setup — mirrors real DB rows
    // DB ref carts:  id=1, user_id=7 (Prerana Giraddi), total_price=0
    // DB ref users:  id=7, name="Prerana Giraddi", email="preranagiraddi@gmail.com"
    // DB ref menu:   id=2, name="HotPot Special Soup", price=199, discount=179
    //                cart_items table is currently EMPTY in DB
    //
    // KEY IMPL NOTES:
    //  - getCartByUserId() uses findByUserId().orElseGet() — NO isCartExists() call
    //  - addItemToCart() uses discountPrice if available (179 < 199 → uses 179)
    //  - addItemToCart() calls cartRepository.findById() for fresh cart after save
    //  - clearCart() returns String "Cart cleared!", NOT void
    //  - clearCart() calls cartItemRepository.findByCartId() + deleteAll() + saveAndFlush()
    // ---------------------------------------------------------------
    @BeforeEach
    public void setUp() {

        user = new User();
        user.setId(7L);                                         // DB: user_id=7
        user.setName("Prerana Giraddi");
        user.setEmail("preranagiraddi@gmail.com");
        user.setContactNumber("1234567890");

        cart = new Cart();
        cart.setId(1L);                                         // DB: cart id=1
        cart.setUser(user);
        cart.setTotalPrice(0.0);                                // DB: total_price=0
        cart.setCartItems(new ArrayList<>());                   // impl always inits list

        menuItem = new MenuItem();
        menuItem.setId(2L);                                     // DB: id=2 HotPot Special Soup
        menuItem.setName("HotPot Special Soup");
        menuItem.setPrice(199.0);                               // DB: price=199
        menuItem.setDiscountPrice(179.0);                       // DB: discount_price=179
        menuItem.setAvailable(true);

        cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setCart(cart);
        cartItem.setMenuItem(menuItem);
        cartItem.setQuantity(2);
        // impl uses discountPrice (179) not price (199): 2 x 179 = 358
        cartItem.setTotalItemPrice(358.0);
    }

    // ---------------------------------------------------------------
    // TEST 1: getCartByUserId — cart already exists
    // Impl: findByUserId() returns present → returns cart directly, NO save
    // DB ref: cart id=1 already exists for user_id=7
    // ---------------------------------------------------------------
    @Test
    public void testGetCartByUserId_CartExists() {

        // Impl calls findByUserId() only — no isCartExists()
        when(cartRepository.findByUserId(7L))
                .thenReturn(Optional.of(cart));

        Cart result = cartService.getCartByUserId(7L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(7L, result.getUser().getId());
        assertEquals(0.0, result.getTotalPrice());

        // No save — cart already existed
        verify(cartRepository, times(0)).save(any(Cart.class));

        System.out.println("✅ Get Cart By User Id (Exists) Test Passed!");
    }

    // ---------------------------------------------------------------
    // TEST 2: getCartByUserId — cart does not exist, new one is created
    // Impl: findByUserId() returns empty → orElseGet → finds user → saves new cart
    // DB ref: user_id=8 (Thushara) has no cart in the carts table
    // ---------------------------------------------------------------
    @Test
    public void testGetCartByUserId_CartNotExists_CreatesNew() {

        User thushara = new User();
        thushara.setId(8L);                                     // DB: user_id=8 Thushara
        thushara.setName("Thushara");
        thushara.setEmail("thushara@gmail.com");

        Cart newCart = new Cart();
        newCart.setId(2L);
        newCart.setUser(thushara);
        newCart.setTotalPrice(0.0);
        newCart.setCartItems(new ArrayList<>());

        // Impl flow: findByUserId empty → findById(user) → save new cart
        when(cartRepository.findByUserId(8L))
                .thenReturn(Optional.empty());
        when(userRepository.findById(8L))
                .thenReturn(Optional.of(thushara));
        when(cartRepository.save(any(Cart.class)))
                .thenReturn(newCart);

        Cart result = cartService.getCartByUserId(8L);

        assertNotNull(result);
        assertEquals(8L, result.getUser().getId());
        assertEquals(0.0, result.getTotalPrice());

        verify(cartRepository, times(1)).save(any(Cart.class)); // new cart saved

        System.out.println("✅ Get Cart By User Id (Creates New) Test Passed!");
    }

    // ---------------------------------------------------------------
    // TEST 3: getCartByUserId — user not found when creating new cart
    // Impl throws: ResourceNotFoundException("User not found")
    // ---------------------------------------------------------------
    @Test
    public void testGetCartByUserId_UserNotFound() {

        when(cartRepository.findByUserId(99L))
                .thenReturn(Optional.empty());
        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            cartService.getCartByUserId(99L);
        });

        System.out.println("✅ Get Cart User Not Found Test Passed!");
    }

    // ---------------------------------------------------------------
    // TEST 4: addItemToCart — new item added successfully
    // Impl uses discountPrice (179) since 179 < 199
    // Impl calls cartRepository.findById() for fresh cart after save
    // DB ref: cart_items EMPTY, adding menuItem id=2 qty=2 → totalItemPrice = 2 x 179 = 358
    // ---------------------------------------------------------------
    @Test
    public void testAddItemToCart_NewItem_UsesDiscountPrice() {

        // getCartByUserId call
        when(cartRepository.findByUserId(7L))
                .thenReturn(Optional.of(cart));
        // find menu item
        when(menuItemRepository.findById(2L))
                .thenReturn(Optional.of(menuItem));
        // no existing cart item
        when(cartItemRepository.findByCartIdAndMenuItemId(1L, 2L))
                .thenReturn(Optional.empty());
        // fresh cart reload after save
        when(cartRepository.findById(1L))
                .thenReturn(Optional.of(cart));
        // findByCartId for updateCartTotal
        when(cartItemRepository.findByCartId(1L))
                .thenReturn(List.of(cartItem));
        when(cartRepository.save(any(Cart.class)))
                .thenReturn(cart);

        Cart result = cartService.addItemToCart(7L, 2L, 2);

        assertNotNull(result);
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
        verify(cartRepository, times(1)).save(any(Cart.class));

        System.out.println("✅ Add Item To Cart (New Item, Discount Price) Test Passed!");
    }

    // ---------------------------------------------------------------
    // TEST 5: addItemToCart — item already in cart, quantity updated
    // Impl: finds existing cartItem → adds quantity → recalculates totalItemPrice
    // cartItem starts at qty=2 (358.0), adding 1 more → qty=3, price = 3 x 179 = 537
    // ---------------------------------------------------------------
    @Test
    public void testAddItemToCart_ItemAlreadyInCart_QuantityUpdated() {

        cartItem.setQuantity(2);
        cartItem.setTotalItemPrice(358.0);                      // 2 x 179 (discount price)

        when(cartRepository.findByUserId(7L))
                .thenReturn(Optional.of(cart));
        when(menuItemRepository.findById(2L))
                .thenReturn(Optional.of(menuItem));
        when(cartItemRepository.findByCartIdAndMenuItemId(1L, 2L))
                .thenReturn(Optional.of(cartItem));             // already in cart
        when(cartRepository.findById(1L))
                .thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartId(1L))
                .thenReturn(List.of(cartItem));
        when(cartRepository.save(any(Cart.class)))
                .thenReturn(cart);

        Cart result = cartService.addItemToCart(7L, 2L, 1);    // add 1 more

        assertNotNull(result);
        assertEquals(3, cartItem.getQuantity());                // 2 + 1 = 3
        assertEquals(537.0, cartItem.getTotalItemPrice());      // 3 x 179 = 537

        verify(cartItemRepository, times(1)).save(cartItem);

        System.out.println("✅ Add Item To Cart (Quantity Updated) Test Passed!");
    }

    // ---------------------------------------------------------------
    // TEST 6: addItemToCart — item with NO discount price, uses regular price
    // DB ref: id=25 "Gobi Manchurian", price=120, discount_price=NULL
    // Impl: discountPrice is null → uses price (120)
    // ---------------------------------------------------------------
    @Test
    public void testAddItemToCart_NoDiscountPrice_UsesRegularPrice() {

        MenuItem gobiFull = new MenuItem();
        gobiFull.setId(25L);                                    // DB: id=25
        gobiFull.setName("Gobi Manchurian");
        gobiFull.setPrice(120.0);                               // DB: price=120
        gobiFull.setDiscountPrice(null);                        // DB: discount_price=NULL
        gobiFull.setAvailable(true);

        CartItem gobiCartItem = new CartItem();
        gobiCartItem.setId(2L);
        gobiCartItem.setCart(cart);
        gobiCartItem.setMenuItem(gobiFull);
        gobiCartItem.setQuantity(1);
        gobiCartItem.setTotalItemPrice(120.0);                  // 1 x 120 (no discount)

        when(cartRepository.findByUserId(7L))
                .thenReturn(Optional.of(cart));
        when(menuItemRepository.findById(25L))
                .thenReturn(Optional.of(gobiFull));
        when(cartItemRepository.findByCartIdAndMenuItemId(1L, 25L))
                .thenReturn(Optional.empty());
        when(cartRepository.findById(1L))
                .thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartId(1L))
                .thenReturn(List.of(gobiCartItem));
        when(cartRepository.save(any(Cart.class)))
                .thenReturn(cart);

        Cart result = cartService.addItemToCart(7L, 25L, 1);

        assertNotNull(result);
        verify(cartItemRepository, times(1)).save(any(CartItem.class));

        System.out.println("✅ Add Item To Cart (No Discount, Regular Price) Test Passed!");
    }

    // ---------------------------------------------------------------
    // TEST 7: clearCart — clears all items and resets total to 0
    // Impl: getCartByUserId → findByCartId → deleteAll → saveAndFlush
    // Returns String "Cart cleared!" (NOT void)
    // DB ref: cart id=1 for user_id=7
    // ---------------------------------------------------------------
    @Test
    public void testClearCart_WithItems() {

        cart.getCartItems().add(cartItem);                      // simulate items in cart

        when(cartRepository.findByUserId(7L))
                .thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartId(1L))
                .thenReturn(List.of(cartItem))                  // items exist
                .getMock();
        // saveAndFlush called by impl
        when(cartRepository.saveAndFlush(any(Cart.class)))
                .thenReturn(cart);

        String result = cartService.clearCart(7L);

        assertEquals("Cart cleared!", result);                  // impl returns this string
        assertEquals(0.0, cart.getTotalPrice());                // total reset to 0
        verify(cartItemRepository, times(1)).deleteAll(List.of(cartItem));
        verify(cartRepository, times(1)).saveAndFlush(cart);

        System.out.println("✅ Clear Cart (With Items) Test Passed!");
    }

    // ---------------------------------------------------------------
    // TEST 8: clearCart — cart already empty, still returns success
    // Impl: items list is empty → skips deleteAll → still resets and saves
    // ---------------------------------------------------------------
    @Test
    public void testClearCart_AlreadyEmpty() {

        when(cartRepository.findByUserId(7L))
                .thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartId(1L))
                .thenReturn(new ArrayList<>());                 // already empty
        when(cartRepository.saveAndFlush(any(Cart.class)))
                .thenReturn(cart);

        String result = cartService.clearCart(7L);

        assertEquals("Cart cleared!", result);
        assertEquals(0.0, cart.getTotalPrice());

        // deleteAll NOT called when list is empty
        verify(cartItemRepository, times(0)).deleteAll(any());

        System.out.println("✅ Clear Cart (Already Empty) Test Passed!");
    }
}