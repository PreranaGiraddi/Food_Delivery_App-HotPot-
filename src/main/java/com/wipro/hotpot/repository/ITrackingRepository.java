package com.wipro.hotpot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wipro.hotpot.entity.Order;
import com.wipro.hotpot.entity.OrderTracking;

@Repository
public interface ITrackingRepository extends JpaRepository<OrderTracking, Long> {

	Optional<OrderTracking> findByOrder(Order order);

	Optional<OrderTracking> findByOrderId(Long orderId);

	List<OrderTracking> findByStatus(OrderTracking.TrackingStatus status);

	@Query("SELECT t FROM OrderTracking t WHERE t.order.user.id = :userId")
	List<OrderTracking> findAllByUserId(Long userId);

	@Query("SELECT t FROM OrderTracking t WHERE t.order.restaurant.id = :restaurantId")
	List<OrderTracking> findAllByRestaurantId(Long restaurantId);

	@Query("SELECT t FROM OrderTracking t WHERE t.order.id = :orderId ORDER BY t.updatedAt DESC")
	List<OrderTracking> findLatestTrackingByOrderId(Long orderId);
}
