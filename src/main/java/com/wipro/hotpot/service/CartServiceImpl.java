package com.wipro.hotpot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.hotpot.dto.CartDTO;
import com.wipro.hotpot.dto.CartItemDTO;
import com.wipro.hotpot.entity.Cart;
import com.wipro.hotpot.entity.CartItem;
import com.wipro.hotpot.entity.MenuItem;
import com.wipro.hotpot.entity.User;
import com.wipro.hotpot.repository.ICartItemRepository;
import com.wipro.hotpot.repository.ICartRepository;
import com.wipro.hotpot.repository.IMenuRepository;
import com.wipro.hotpot.repository.IUserRepository;

@Service
public class CartServiceImpl implements ICartService {

	@Autowired
	private ICartRepository cartRepository;

	@Autowired
	private ICartItemRepository cartItemRepository;

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private IMenuRepository menuRepository;

	
	@Override
	public Cart getCartByUserId(Long userId) {
		if (!cartRepository.isCartExists(userId)) {
		    User user = userRepository.findById(userId)
		            .orElseThrow(() -> new RuntimeException("User not found!"));
		    Cart newCart = new Cart();
		    newCart.setUser(user);
		    newCart.setTotalPrice(0.0);
		    cartRepository.save(newCart);
		}
		return cartRepository.findByUserId(userId)
		        .orElseThrow(() -> new RuntimeException("Cart not found!"));
		
	}

	// ✅ Add item to cart
	@Override
	public Cart addItemToCart(Long userId, Long menuItemId, Integer quantity) {

		Cart cart = getCartByUserId(userId);

		MenuItem menuItem = menuRepository.findById(menuItemId)
				.orElseThrow(() -> new RuntimeException("Menu item not found!"));

		// Check if item already in cart
		Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndMenuItemId(cart.getId(), menuItemId);

		if (existingItem.isPresent()) {
			// Update quantity if already exists
			CartItem cartItem = existingItem.get();
			cartItem.setQuantity(cartItem.getQuantity() + quantity);
			cartItem.setTotalItemPrice(cartItem.getQuantity() * menuItem.getPrice());
			cartItemRepository.save(cartItem);
		} else {
			// Add new item
			CartItem cartItem = new CartItem();
			cartItem.setCart(cart);
			cartItem.setMenuItem(menuItem);
			cartItem.setQuantity(quantity);
			cartItem.setTotalItemPrice(quantity * menuItem.getPrice());
			cartItemRepository.save(cartItem);
		}

		// Update total price
		updateCartTotal(cart);
		return cartRepository.save(cart);
	}

	// ✅ Remove item from cart
	@Override
	public Cart removeItemFromCart(Long userId, Long menuItemId) {
		Cart cart = getCartByUserId(userId);
		cartItemRepository.deleteByCartIdAndMenuItemId(cart.getId(), menuItemId);
		updateCartTotal(cart);
		return cartRepository.save(cart);
	}

	// ✅ Update item quantity
	@Override
	public Cart updateItemQuantity(Long userId, Long menuItemId, Integer quantity) {
		Cart cart = getCartByUserId(userId);

		CartItem cartItem = cartItemRepository.findByCartIdAndMenuItemId(cart.getId(), menuItemId)
				.orElseThrow(() -> new RuntimeException("Item not in cart!"));

		if (quantity <= 0) {
			cartItemRepository.delete(cartItem);
		} else {
			cartItem.setQuantity(quantity);
			cartItem.setTotalItemPrice(quantity * cartItem.getMenuItem().getPrice());
			cartItemRepository.save(cartItem);
		}

		updateCartTotal(cart);
		return cartRepository.save(cart);
	}

	// ✅ Clear cart
	@Override
	public void clearCart(Long userId) {
		Cart cart = getCartByUserId(userId);
		cartItemRepository.deleteByCartId(cart.getId());
		cart.setTotalPrice(0.0);
		cartRepository.save(cart);
	}

	// ✅ Get cart details as DTO
	@Override
	public CartDTO getCartDetails(Long userId) {
		Cart cart = getCartByUserId(userId);
		List<CartItem> items = cartItemRepository.findByCartId(cart.getId());

		List<CartItemDTO> itemDTOs = new ArrayList<>();
		for (CartItem item : items) {
			CartItemDTO dto = new CartItemDTO();
			dto.setMenuItemId(item.getMenuItem().getId());
			dto.setMenuItemName(item.getMenuItem().getName());
			dto.setQuantity(item.getQuantity());
			dto.setPrice(item.getMenuItem().getPrice());
			dto.setTotalItemPrice(item.getTotalItemPrice());
			itemDTOs.add(dto);
		}

		CartDTO cartDTO = new CartDTO();
		cartDTO.setCartId(cart.getId());
		cartDTO.setUserId(userId);
		cartDTO.setCartItems(itemDTOs);
		cartDTO.setTotalPrice(cart.getTotalPrice());

		return cartDTO;
	}

	// ✅ Helper - update cart total
	private void updateCartTotal(Cart cart) {
		List<CartItem> items = cartItemRepository.findByCartId(cart.getId());
		double total = 0.0;
		for (CartItem item : items) {
			total += item.getTotalItemPrice();
		}
		cart.setTotalPrice(total);
	}
}