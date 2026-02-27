package com.wipro.hotpot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wipro.hotpot.entity.Order;
import com.wipro.hotpot.entity.Restaurant;
import com.wipro.hotpot.entity.User;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByUser(User user);

	List<Order> findByUserId(Long userId);

	List<Order> findByRestaurant(Restaurant restaurant);

	List<Order> findByRestaurantId(Long restaurantId);

	List<Order> findByStatus(Order.OrderStatus status);

	List<Order> findByUserIdAndStatus(Long userId, Order.OrderStatus status);

	List<Order> findByRestaurantIdAndStatus(Long restaurantId, Order.OrderStatus status);

	@Query("SELECT o FROM Order o WHERE o.user.id = :userId ORDER BY o.orderedAt DESC")
	List<Order> findOrderHistoryByUser(Long userId);

	@Query("SELECT o FROM Order o WHERE o.restaurant.id = :restaurantId ORDER BY o.orderedAt DESC")
	List<Order> findOrdersByRestaurant(Long restaurantId);
}