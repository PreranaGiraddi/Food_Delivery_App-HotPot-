package com.wipro.hotpot.service;

import com.wipro.hotpot.dto.CartDTO;
import com.wipro.hotpot.entity.Cart;

public interface ICartService {

	Cart getCartByUserId(Long userId);

	Cart addItemToCart(Long userId, Long menuItemId, Integer quantity);

	Cart removeItemFromCart(Long userId, Long menuItemId);

	Cart updateItemQuantity(Long userId, Long menuItemId, Integer quantity);

	void clearCart(Long userId);

	CartDTO getCartDetails(Long userId);
}