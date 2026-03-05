package com.wipro.hotpot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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


    @Transactional
    @Modifying
    void deleteByCart(Cart cart);

    @Transactional
    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.cart.id = :cartId")
    void deleteByCartId(@Param("cartId") Long cartId);


    @Transactional
    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.cart.id = :cartId AND ci.menuItem.id = :menuItemId")
    void deleteByCartIdAndMenuItemId(@Param("cartId") Long cartId,
                                     @Param("menuItemId") Long menuItemId);
}