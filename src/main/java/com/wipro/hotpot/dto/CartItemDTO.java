package com.wipro.hotpot.dto;

public class CartItemDTO {

	private Long menuItemId;
	private String menuItemName;
	private Integer quantity;
	private Double price;
	private Double totalItemPrice;

	public CartItemDTO() {
	}

	public CartItemDTO(Long menuItemId, String menuItemName, Integer quantity, Double price, Double totalItemPrice) {
		this.menuItemId = menuItemId;
		this.menuItemName = menuItemName;
		this.quantity = quantity;
		this.price = price;
		this.totalItemPrice = totalItemPrice;
	}

	public Long getMenuItemId() {
		return menuItemId;
	}

	public String getMenuItemName() {
		return menuItemName;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public Double getPrice() {
		return price;
	}

	public Double getTotalItemPrice() {
		return totalItemPrice;
	}

	public void setMenuItemId(Long menuItemId) {
		this.menuItemId = menuItemId;
	}

	public void setMenuItemName(String menuItemName) {
		this.menuItemName = menuItemName;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public void setTotalItemPrice(Double totalItemPrice) {
		this.totalItemPrice = totalItemPrice;
	}
}