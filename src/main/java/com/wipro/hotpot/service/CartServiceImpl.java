package com.wipro.hotpot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wipro.hotpot.dto.CartDTO;
import com.wipro.hotpot.dto.CartItemDTO;
import com.wipro.hotpot.entity.Cart;
import com.wipro.hotpot.entity.CartItem;
import com.wipro.hotpot.entity.MenuItem;
import com.wipro.hotpot.entity.User;
import com.wipro.hotpot.exception.ResourceNotFoundException;
import com.wipro.hotpot.repository.ICartItemRepository;
import com.wipro.hotpot.repository.ICartRepository;
import com.wipro.hotpot.repository.IMenuRepository;
import com.wipro.hotpot.repository.IUserRepository;

@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    private ICartRepository cartRepository;

    @Autowired
    private ICartItemRepository cartItemRepository;

    @Autowired
    private IMenuRepository menuItemRepository;

    @Autowired
    private IUserRepository userRepository;

  
    @Transactional
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
            .orElseGet(() -> {
                Cart newCart = new Cart();
                newCart.setUser(userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found")));
                newCart.setTotalPrice(0.0);
                newCart.setCartItems(new ArrayList<>()); 
                return cartRepository.save(newCart);
            });
    }

    
    @Override
    @Transactional
    public Cart addItemToCart(Long userId, Long menuItemId, Integer quantity) {
        Cart cart = getCartByUserId(userId);

        MenuItem menuItem = menuItemRepository.findById(menuItemId)
            .orElseThrow(() -> new ResourceNotFoundException("Menu item not found!"));

        
        Double priceToUse = (menuItem.getDiscountPrice() != null
                && menuItem.getDiscountPrice() < menuItem.getPrice())
                ? menuItem.getDiscountPrice()
                : menuItem.getPrice();

        Optional<CartItem> existing =
            cartItemRepository.findByCartIdAndMenuItemId(cart.getId(), menuItemId);

        if (existing.isPresent()) {
            CartItem cartItem = existing.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setTotalItemPrice(priceToUse * cartItem.getQuantity());
            cartItemRepository.save(cartItem);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setMenuItem(menuItem);
            cartItem.setQuantity(quantity);
            cartItem.setTotalItemPrice(priceToUse * quantity);
            cartItemRepository.save(cartItem);
        }

       
        Cart freshCart = cartRepository.findById(cart.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Cart not found!"));
        updateCartTotal(freshCart);
        return cartRepository.save(freshCart);
    }

    
    @Override
    @Transactional
    public Cart removeItemFromCart(Long userId, Long menuItemId) {
        Cart cart = getCartByUserId(userId);

        cartItemRepository.deleteByCartIdAndMenuItemId(cart.getId(), menuItemId);
        cartItemRepository.flush(); // ✅ Force DB delete immediately

        Cart freshCart = cartRepository.findById(cart.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Cart not found!"));

        updateCartTotal(freshCart);
        return cartRepository.save(freshCart);
    }

    
    @Override
    @Transactional
    public Cart updateItemQuantity(Long userId, Long menuItemId, Integer quantity) {
        Cart cart = getCartByUserId(userId);

        CartItem cartItem = cartItemRepository
            .findByCartIdAndMenuItemId(cart.getId(), menuItemId)
            .orElseThrow(() -> new ResourceNotFoundException("Item not in cart!"));

        if (quantity <= 0) {
            cartItemRepository.deleteByCartIdAndMenuItemId(cart.getId(), menuItemId);
            cartItemRepository.flush(); // ✅ Force DB delete
        } else {
            MenuItem menuItem = cartItem.getMenuItem();
            Double priceToUse = (menuItem.getDiscountPrice() != null
                    && menuItem.getDiscountPrice() < menuItem.getPrice())
                    ? menuItem.getDiscountPrice()
                    : menuItem.getPrice();

            cartItem.setQuantity(quantity);
            cartItem.setTotalItemPrice(priceToUse * quantity);
            cartItemRepository.save(cartItem);
        }

        Cart freshCart = cartRepository.findById(cart.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Cart not found!"));

        updateCartTotal(freshCart);
        return cartRepository.save(freshCart);
    }

    
    @Override
    @Transactional
    public String clearCart(Long userId) {
        Cart cart = getCartByUserId(userId);

        System.out.println("Clearing cartId: " + cart.getId());

        List<CartItem> items = cartItemRepository.findByCartId(cart.getId());
        System.out.println("Items to delete: " + items.size());

        if (!items.isEmpty()) {
            cartItemRepository.deleteAll(items);
            cartItemRepository.flush(); 
        }

       
        cart.getCartItems().clear();
        cart.setTotalPrice(0.0);
        cartRepository.saveAndFlush(cart);

       
        List<CartItem> remaining = cartItemRepository.findByCartId(cart.getId());
        System.out.println("Items remaining after delete: " + remaining.size());

        return "Cart cleared!";
    }

   
    @Override
    @Transactional 
    public CartDTO getCartDetails(Long userId) {
        Cart cart = getCartByUserId(userId);

        
        List<CartItem> items = cartItemRepository.findByCartId(cart.getId());

        CartDTO cartDTO = new CartDTO();
        cartDTO.setCartId(cart.getId());
        cartDTO.setUserId(userId);

        if (items == null || items.isEmpty()) {
           
            cartDTO.setTotalPrice(0.0);
            cartDTO.setCartItems(new ArrayList<>());
            return cartDTO;
        }

        
        double freshTotal = items.stream()
            .mapToDouble(i -> i.getTotalItemPrice() != null ? i.getTotalItemPrice() : 0.0)
            .sum();

        cartDTO.setTotalPrice(freshTotal);

        
        if (Math.abs(freshTotal - cart.getTotalPrice()) > 0.01) {
            cart.setTotalPrice(freshTotal);
            cartRepository.save(cart);
        }

        List<CartItemDTO> itemDTOs = items.stream().map(item -> {
            CartItemDTO dto = new CartItemDTO();
            dto.setMenuItemId(item.getMenuItem().getId());
            dto.setMenuItemName(item.getMenuItem().getName());
            dto.setQuantity(item.getQuantity());

            Double price = (item.getMenuItem().getDiscountPrice() != null
                && item.getMenuItem().getDiscountPrice() < item.getMenuItem().getPrice())
                ? item.getMenuItem().getDiscountPrice()
                : item.getMenuItem().getPrice();

            dto.setPrice(price);
            dto.setTotalItemPrice(item.getTotalItemPrice());
            return dto;
        }).toList();

        cartDTO.setCartItems(itemDTOs);
        return cartDTO;
    }

   
    private void updateCartTotal(Cart cart) {
        List<CartItem> items = cartItemRepository.findByCartId(cart.getId());
        double total = items.stream()
            .mapToDouble(i -> i.getTotalItemPrice() != null ? i.getTotalItemPrice() : 0.0)
            .sum();
        cart.setTotalPrice(total);
    }

    @Override
    public boolean isCartExists(Long userId) {
        return cartRepository.isCartExists(userId);
    }
}