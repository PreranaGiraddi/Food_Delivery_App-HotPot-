package com.wipro.hotpot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wipro.hotpot.dto.CartDTO;
import com.wipro.hotpot.entity.Cart;
import com.wipro.hotpot.service.ICartService;

import org.springframework.transaction.annotation.Transactional;
@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private ICartService cartService;

    
    @GetMapping("/{userId}")
    public ResponseEntity<CartDTO> getCartDetails(@PathVariable Long userId) {
        CartDTO cartDTO = cartService.getCartDetails(userId);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    
    @PostMapping("/add")
    public ResponseEntity<Cart> addItemToCart(@RequestParam Long userId,
                                              @RequestParam Long menuItemId,
                                              @RequestParam Integer quantity) {
        Cart cart = cartService.addItemToCart(userId, menuItemId, quantity);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/remove")
    public ResponseEntity<CartDTO> removeItemFromCart(
            @RequestParam Long userId,
            @RequestParam Long menuItemId) {
        cartService.removeItemFromCart(userId, menuItemId);
       
        return ResponseEntity.ok(cartService.getCartDetails(userId));
    }
    @Transactional
    @PutMapping("/update")
    public ResponseEntity<CartDTO> updateItemQuantity(
            @RequestParam Long userId,
            @RequestParam Long menuItemId,
            @RequestParam Integer quantity) {
        cartService.updateItemQuantity(userId, menuItemId, quantity);
        return ResponseEntity.ok(cartService.getCartDetails(userId));
    }

    @Transactional
    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<String> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return new ResponseEntity<>("Cart cleared successfully!", HttpStatus.OK);
    }
}