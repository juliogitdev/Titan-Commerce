package com.titan.commerce.modules.cart.repository;

import com.titan.commerce.modules.cart.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {


    Optional<CartItem> findByCartIdAndProductVariantId(String cartId, Long productVariantId);


}