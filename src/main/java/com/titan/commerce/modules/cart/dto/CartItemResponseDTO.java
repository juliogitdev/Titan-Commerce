package com.titan.commerce.modules.cart.dto;

import com.titan.commerce.modules.cart.domain.CartItem;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Representação de um item dentro do carrinho")
public record CartItemResponseDTO(
        @Schema(description = "ID único do item no carrinho", example = "10")
        Long id,

        @Schema(description = "ID da variante do produto", example = "340")
        Long productVariantId,

        @Schema(description = "Nome do produto", example = "Produto #340")
        String productName,

        @Schema(description = "Quantidade selecionada", example = "2")
        Integer quantity,

        @Schema(description = "Preço unitário no momento da adição", example = "3599.99")
        BigDecimal unitPrice,

        @Schema(description = "Subtotal deste item (Quantidade x Preço Unitário)", example = "7199.98")
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