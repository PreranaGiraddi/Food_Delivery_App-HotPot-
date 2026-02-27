package com.hotpot.service.cart;

import com.hotpot.dto.cart.OrderDTO;
import com.hotpot.entity.cart.Order;
import java.util.List;

public interface OrderService {

    Order placeOrder(Long userId, OrderDTO dto);

    Order getOrderById(Long orderId);

    List<Order> getOrdersByUser(Long userId);

    List<Order> getOrdersByRestaurant(Long restaurantId);

    List<Order> getOrderHistory(Long userId);

    Order cancelOrder(Long orderId);
}