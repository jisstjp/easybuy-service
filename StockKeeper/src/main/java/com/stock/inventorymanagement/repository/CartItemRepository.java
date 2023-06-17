package com.stock.inventorymanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.stock.inventorymanagement.domain.CartItem;
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long>{
    
    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.id = :cartId AND ci.deleted = false")
    List<CartItem> findByCartIdWithoutDeleted(@Param("cartId") Long cartId);
    
    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.id = :cartId AND ci.id = :itemId AND ci.deleted = false")
    Optional<CartItem> findByCartIdAndItemIdWithoutDeleted(@Param("cartId") Long cartId, @Param("itemId") Long itemId);
    
    @Modifying
    @Query("UPDATE CartItem ci SET ci.deleted = true WHERE ci.cart.id = :cartId AND ci.deleted = false")
    void markCartItemsAsDeleted(@Param("cartId") Long cartId);

}
