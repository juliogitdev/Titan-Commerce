package com.titan.commerce.modules.cart.dto;

import com.titan.commerce.modules.cart.domain.Cart;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "Representação completa do carrinho de compras")
public record CartResponseDTO(

        @Schema(description = "ID único do carrinho (usado no Cookie para usuários anônimos)", example = "a1b2c3d4-e5f6-7890")
        String id,

        @Schema(description = "Lista de itens atualmente no carrinho")
        List<CartItemResponseDTO> items,

        @Schema(description = "Valor total de todos os itens no carrinho", example = "7199.98")
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