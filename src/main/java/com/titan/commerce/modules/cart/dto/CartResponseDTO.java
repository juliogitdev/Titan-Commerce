package com.titan.commerce.modules.cart.dto;

import com.titan.commerce.modules.cart.domain.Cart;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public record CartResponseDTO(
        String id,
        List<CartItemResponseDTO> items,
        BigDecimal totalValue
) {
    public static CartResponseDTO fromEntity(Cart cart) {
        List<CartItemResponseDTO> itemDTOs = cart.getItems().stream()
                .map(CartItemResponseDTO::fromEntity)
                .collect(Collectors.toList());

        BigDecimal total = itemDTOs.stream()
                .map(CartItemResponseDTO::subTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponseDTO(cart.getId(), itemDTOs, total);
    }
}