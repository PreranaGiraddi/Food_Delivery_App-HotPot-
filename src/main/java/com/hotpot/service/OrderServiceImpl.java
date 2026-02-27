package com.hotpot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotpot.dto.OrderDTO;
import com.hotpot.entity.Cart;
import com.hotpot.entity.CartItem;
import com.hotpot.entity.Order;
import com.hotpot.entity.OrderItem;
import com.hotpot.entity.Restaurant;
import com.hotpot.entity.User;
import com.hotpot.repository.CartItemRepository;
import com.hotpot.repository.CartRepository;
import com.hotpot.repository.OrderRepository;
import com.hotpot.repository.RestaurantRepository;
import com.hotpot.repository.UserRepository;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RestaurantRepository restaurantRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private CartService cartService;

	@Autowired
	private EmailService emailService;

	// ✅ Place order
	@Override
	public Order placeOrder(Long userId, OrderDTO dto) {

		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found!"));

		Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
				.orElseThrow(() -> new RuntimeException("Restaurant not found!"));

		Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Cart is empty!"));

		List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());

		if (cartItems.isEmpty()) {
			throw new RuntimeException("Cart is empty! Add items before ordering.");
		}

		// Create order
		Order order = new Order();
		order.setUser(user);
		order.setRestaurant(restaurant);
		order.setDeliveryAddress(dto.getDeliveryAddress());
		order.setPaymentMethod(dto.getPaymentMethod());
		order.setTotalAmount(cart.getTotalPrice());
		order.setStatus(Order.OrderStatus.PLACED);

		// Create order items from cart items
		List<OrderItem> orderItems = new ArrayList<>();
		for (CartItem cartItem : cartItems) {
			OrderItem orderItem = new OrderItem();
			orderItem.setOrder(order);
			orderItem.setMenuItem(cartItem.getMenuItem());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setPrice(cartItem.getTotalItemPrice());
			orderItems.add(orderItem);
		}
		order.setOrderItems(orderItems);

		Order savedOrder = orderRepository.save(order);

		// Clear cart after order placed
		cartService.clearCart(userId);

		// Send confirmation email
		emailService.sendOrderConfirmationEmail(user.getEmail(), user.getName(), savedOrder.getId());

		return savedOrder;
	}

	// ✅ Get order by id
	@Override
	public Order getOrderById(Long orderId) {
		return orderRepository.findById(orderId)
				.orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
	}

	// ✅ Get orders by user
	@Override
	public List<Order> getOrdersByUser(Long userId) {
		return orderRepository.findByUserId(userId);
	}

	// ✅ Get orders by restaurant
	@Override
	public List<Order> getOrdersByRestaurant(Long restaurantId) {
		return orderRepository.findByRestaurantId(restaurantId);
	}

	// ✅ Get order history
	@Override
	public List<Order> getOrderHistory(Long userId) {
		return orderRepository.findOrderHistoryByUser(userId);
	}

	// ✅ Cancel order
	@Override
	public Order cancelOrder(Long orderId) {
		Order order = getOrderById(orderId);
		if (order.getStatus() == Order.OrderStatus.DELIVERED) {
			throw new RuntimeException("Delivered orders cannot be cancelled!");
		}
		order.setStatus(Order.OrderStatus.CANCELLED);
		return orderRepository.save(order);
	}
}