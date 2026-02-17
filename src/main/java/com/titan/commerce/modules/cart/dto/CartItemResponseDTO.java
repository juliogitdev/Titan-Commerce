package com.titan.commerce.modules.cart.dto;

import com.titan.commerce.modules.cart.domain.CartItem;
import java.math.BigDecimal;

public record CartItemResponseDTO(
        Long id,
        Long productVariantId,
        String productName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subTotal
) {
    public static CartItemResponseDTO fromEntity(CartItem item) {
        return new CartItemResponseDTO(
                item.getId(),
                item.getProductVariant().getId(),
                "Produto #" + item.getProductVariant().getId(),
                item.getQuantity(),
                item.getPrice(),
                item.getSubTotal()
        );
    }
}