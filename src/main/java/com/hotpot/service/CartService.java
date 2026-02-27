package com.hotpot.service;

import com.hotpot.dto.CartDTO;
import com.hotpot.entity.Cart;

public interface CartService {

	Cart getCartByUserId(Long userId);

	Cart addItemToCart(Long userId, Long menuItemId, Integer quantity);

	Cart removeItemFromCart(Long userId, Long menuItemId);

	Cart updateItemQuantity(Long userId, Long menuItemId, Integer quantity);

	void clearCart(Long userId);

	CartDTO getCartDetails(Long userId);
}