package com.hotpot.repository.tracking;

import com.hotpot.entity.tracking.OrderTracking;
import com.hotpot.entity.cart.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrackingRepository extends JpaRepository<OrderTracking, Long> {

    // ✅ Find tracking by order
    Optional<OrderTracking> findByOrder(Order order);

    // ✅ Find tracking by order id
    Optional<OrderTracking> findByOrderId(Long orderId);

    // ✅ Find all trackings by status
    List<OrderTracking> findByStatus(OrderTracking.TrackingStatus status);

    // ✅ Find tracking by order's user id
    @Query("SELECT t FROM OrderTracking t WHERE t.order.user.id = :userId")
    List<OrderTracking> findAllByUserId(Long userId);

    // ✅ Find tracking by order's restaurant id
    @Query("SELECT t FROM OrderTracking t WHERE t.order.restaurant.id = :restaurantId")
    List<OrderTracking> findAllByRestaurantId(Long restaurantId);

    // ✅ Find latest tracking status of an order
    @Query("SELECT t FROM OrderTracking t WHERE t.order.id = :orderId ORDER BY t.updatedAt DESC")
    List<OrderTracking> findLatestTrackingByOrderId(Long orderId);
}

