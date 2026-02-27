package com.wipro.hotpot.service;

import java.util.List;

import com.wipro.hotpot.dto.OrderDTO;
import com.wipro.hotpot.entity.Order;

public interface IOrderService {

	Order placeOrder(Long userId, OrderDTO dto);

	Order getOrderById(Long orderId);

	List<Order> getOrdersByUser(Long userId);

	List<Order> getOrdersByRestaurant(Long restaurantId);

	List<Order> getOrderHistory(Long userId);

	Order cancelOrder(Long orderId);
}