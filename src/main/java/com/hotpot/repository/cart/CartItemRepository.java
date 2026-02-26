package com.hotpot.repository.cart;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hotpot.entity.cart.Cart;
import com.hotpot.entity.cart.CartItem;
import com.hotpot.entity.menu.MenuItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // ✅ Find all items in a cart
    List<CartItem> findByCart(Cart cart);

    // ✅ Find all items by cart id
    List<CartItem> findByCartId(Long cartId);

    // ✅ Find specific item in cart
    Optional<CartItem> findByCartAndMenuItem(Cart cart, MenuItem menuItem);

    // ✅ Find cart item by cart id and menu item id
    Optional<CartItem> findByCartIdAndMenuItemId(Long cartId, Long menuItemId);

    // ✅ Delete all items in a cart (after order placed)
    void deleteByCart(Cart cart);

    // ✅ Delete all items by cart id
    void deleteByCartId(Long cartId);
}