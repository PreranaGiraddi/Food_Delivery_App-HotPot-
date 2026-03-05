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

        // Step 1 — Find user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        // Step 2 — Find restaurant
        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found!"));

        // Step 3 — Get cart
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart is empty!"));

        // Step 4 — Get cart items
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());

        // Step 5 — Check cart not empty
        if (cartItems == null || cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty! Add items before ordering.");
        }

        // ✅ Step 6 — Check all items are from same restaurant
        for (CartItem cartItem : cartItems) {
            Long itemRestaurantId = cartItem.getMenuItem()
                                            .getRestaurant()
                                            .getId();

            if (!itemRestaurantId.equals(dto.getRestaurantId())) {
                throw new RuntimeException(
                    "Your cart has items from multiple restaurants! " +
                    "Please clear your cart and order from one restaurant at a time."
                );
            }
        }

        // Step 7 — Create order
        Order order = new Order();
        order.setUser(user);
        order.setRestaurant(restaurant);
        order.setDeliveryAddress(dto.getDeliveryAddress());
        order.setPaymentMethod(dto.getPaymentMethod());
        order.setTotalAmount(cart.getTotalPrice());
        order.setStatus(Order.OrderStatus.PLACED);

        // Step 8 — Convert cart items to order items
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

        // Step 9 — Save order
        Order savedOrder = orderRepository.save(order);

        // Step 10 — Clear cart after order
        cartItemRepository.deleteByCartId(cart.getId());
        cart.setTotalPrice(0.0);
        cartRepository.save(cart);

        // Step 11 — Send confirmation email
        try {
            emailService.sendOrderConfirmationEmail(
                user.getEmail(), user.getName(), savedOrder.getId()
            );
        } catch (Exception e) {
            System.out.println("Email sending failed: " + e.getMessage());
        }

        return savedOrder;
    }


    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Order not found with id: " + orderId));
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