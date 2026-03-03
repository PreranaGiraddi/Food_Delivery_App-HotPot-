package com.wipro.hotpot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.wipro.hotpot.entity.Cart;
import com.wipro.hotpot.entity.CartItem;
import com.wipro.hotpot.entity.MenuItem;

@Repository
public interface ICartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByCart(Cart cart);

    List<CartItem> findByCartId(Long cartId);

    Optional<CartItem> findByCartAndMenuItem(Cart cart, MenuItem menuItem);

    Optional<CartItem> findByCartIdAndMenuItemId(Long cartId, Long menuItemId);

    // ✅ Add @Transactional and @Modifying here
    @Transactional
    @Modifying
    void deleteByCart(Cart cart);

    // ✅ Add @Transactional and @Modifying here
    @Transactional
    @Modifying
    void deleteByCartId(Long cartId);

    // ✅ Add @Transactional and @Modifying here
    @Transactional
    @Modifying
    void deleteByCartIdAndMenuItemId(Long cartId, Long menuItemId);
}
