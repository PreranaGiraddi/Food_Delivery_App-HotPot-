package com.wipro.hotpot.dto;

import java.util.List;

public class CartDTO {

	private Long cartId;
	private Long userId;
	private List<CartItemDTO> cartItems;
	private Double totalPrice;

	public CartDTO() {
	}

	public CartDTO(Long cartId, Long userId, List<CartItemDTO> cartItems, Double totalPrice) {
		this.cartId = cartId;
		this.userId = userId;
		this.cartItems = cartItems;
		this.totalPrice = totalPrice;
	}

	public Long getCartId() {
		return cartId;
	}

	public Long getUserId() {
		return userId;
	}

	public List<CartItemDTO> getCartItems() {
		return cartItems;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setCartItems(List<CartItemDTO> cartItems) {
		this.cartItems = cartItems;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
}