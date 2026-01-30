package com.titan.commerce.modules.cart.repository;

import com.titan.commerce.modules.cart.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    /**
     * Busca um item específico dentro de um carrinho.
     * @param cartId O UUID do carrinho (String)
     * @param productVariantId O ID da variante do produto
     * @return Optional contendo o item se existir
     */
    Optional<CartItem> findByCartIdAndProductVariantId(String cartId, Long productVariantId);



}