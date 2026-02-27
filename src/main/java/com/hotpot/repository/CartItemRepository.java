package com.hotpot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hotpot.entity.Cart;
import com.hotpot.entity.CartItem;
import com.hotpot.entity.MenuItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	List<CartItem> findByCart(Cart cart);

	List<CartItem> findByCartId(Long cartId);

	Optional<CartItem> findByCartAndMenuItem(Cart cart, MenuItem menuItem);

	Optional<CartItem> findByCartIdAndMenuItemId(Long cartId, Long menuItemId);

	void deleteByCart(Cart cart);

	void deleteByCartId(Long cartId);

	void deleteByCartIdAndMenuItemId(Long cartId, Long menuItemId);
}