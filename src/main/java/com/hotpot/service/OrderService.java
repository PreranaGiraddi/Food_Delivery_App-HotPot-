package com.hotpot.service;

import java.util.List;

import com.hotpot.dto.OrderDTO;
import com.hotpot.entity.Order;

public interface OrderService {

	Order placeOrder(Long userId, OrderDTO dto);

	Order getOrderById(Long orderId);

	List<Order> getOrdersByUser(Long userId);

	List<Order> getOrdersByRestaurant(Long restaurantId);

	List<Order> getOrderHistory(Long userId);

	Order cancelOrder(Long orderId);
}