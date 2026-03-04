package com.wipro.hotpot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.hotpot.dto.OrderDTO;
import com.wipro.hotpot.entity.Cart;
import com.wipro.hotpot.entity.CartItem;
import com.wipro.hotpot.entity.Order;
import com.wipro.hotpot.entity.OrderItem;
import com.wipro.hotpot.entity.Restaurant;
import com.wipro.hotpot.entity.User;
import com.wipro.hotpot.exception.ResourceNotFoundException;
import com.wipro.hotpot.repository.ICartItemRepository;
import com.wipro.hotpot.repository.ICartRepository;
import com.wipro.hotpot.repository.IOrderRepository;
import com.wipro.hotpot.repository.IRestaurantRepository;
import com.wipro.hotpot.repository.IUserRepository;

@Service
public class OrderServiceImpl implements IOrderService {

	@Autowired
	private IOrderRepository orderRepository;

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private IRestaurantRepository restaurantRepository;

	@Autowired
	private ICartRepository cartRepository;

	@Autowired
	private ICartItemRepository cartItemRepository;

	@Autowired
	private ICartService cartService;

	@Autowired
	private EmailService emailService;

	
	@Override
	public Order placeOrder(Long userId, OrderDTO dto) {

		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found!"));

		Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
				.orElseThrow(() -> new ResourceNotFoundException("Restaurant not found!"));

		Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Cart is empty!"));

		List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());

		if (cartItems.isEmpty()) {
			throw new RuntimeException("Cart is empty! Add items before ordering.");
		}

		
		Order order = new Order();
		order.setUser(user);
		order.setRestaurant(restaurant);
		order.setDeliveryAddress(dto.getDeliveryAddress());
		order.setPaymentMethod(dto.getPaymentMethod());
		order.setTotalAmount(cart.getTotalPrice());
		order.setStatus(Order.OrderStatus.PLACED);

		
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

		
		cartService.clearCart(userId);

		
		emailService.sendOrderConfirmationEmail(user.getEmail(), user.getName(), savedOrder.getId());

		return savedOrder;
	}


	@Override
	public Order getOrderById(Long orderId) {
		return orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
	}

	
	@Override
	public List<Order> getOrdersByUser(Long userId) {
		return orderRepository.findByUserId(userId);
	}

	
	@Override
	public List<Order> getOrdersByRestaurant(Long restaurantId) {
		return orderRepository.findByRestaurantId(restaurantId);
	}

	
	@Override
	public List<Order> getOrderHistory(Long userId) {
		return orderRepository.findOrderHistoryByUser(userId);
	}


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