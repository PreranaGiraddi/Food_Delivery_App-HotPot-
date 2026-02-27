package com.hotpot.service.cart;

import com.hotpot.dto.cart.CartDTO;
import com.hotpot.entity.cart.Cart;

public interface CartService {

    Cart getCartByUserId(Long userId);

    Cart addItemToCart(Long userId, Long menuItemId, Integer quantity);

    Cart removeItemFromCart(Long userId, Long menuItemId);

    Cart updateItemQuantity(Long userId, Long menuItemId, Integer quantity);

    void clearCart(Long userId);

    CartDTO getCartDetails(Long userId);
}