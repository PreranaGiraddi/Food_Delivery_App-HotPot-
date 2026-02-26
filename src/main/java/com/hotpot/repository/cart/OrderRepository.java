package com.hotpot.repository.cart;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hotpot.entity.auth.User;
import com.hotpot.entity.cart.Order;
import com.hotpot.entity.menu.Restaurant;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    
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