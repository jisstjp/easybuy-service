package com.stock.inventorymanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.stock.inventorymanagement.domain.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("SELECT c FROM Cart c WHERE c.id = :cartId AND c.user.id = :userId AND c.isDeleted = false")
    Optional<Cart> findByIdAndUserId(Long cartId, Long userId);

    List<Cart> findByUserId(Long userId);

    void deleteByIdAndUserId(Long cartId, Long userId);
    
    Optional<Cart> findByIdAndIsDeletedFalse(Long cartId);
    
    @Query("SELECT c FROM Cart c WHERE c.user.id = :userId AND c.isDeleted = false")
    List<Cart> findNonDeletedCartsByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE Cart c SET c.isDeleted = true WHERE c.id = :cartId AND c.isDeleted = false")
    void markCartAsDeleted(@Param("cartId") Long cartId);


    boolean existsByUserId(Long userId);

}
