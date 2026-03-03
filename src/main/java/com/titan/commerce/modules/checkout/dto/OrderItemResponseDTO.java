package com.titan.commerce.modules.checkout.dto;

import com.titan.commerce.modules.checkout.domain.OrderItem;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Detalhes de um item comprado dentro do pedido")
public record OrderItemResponseDTO(

        @Schema(description = "ID da variante comprada", example = "340")
        Long productVariantId,

        @Schema(description = "Nome do produto no momento da compra", example = "Notebook Gamer Pro 15")
        String productName,

        @Schema(description = "Quantidade comprada", example = "1")
        Integer quantity,

        @Schema(description = "Preço unitário pago (congelado no momento da compra)", example = "3599.99")
        BigDecimal unitPrice,

        @Schema(description = "Subtotal deste item", example = "3599.99")
        BigDecimal subTotal
) {
    public OrderItemResponseDTO(OrderItem item)
    {
    this(
            item.getProductVariant().getId(),
            item.getProductVariant().getProduct().getTitle(), // Navega até o nome do produto
            item.getQuantity(),
            item.getUnitPrice(),
            item.getSubTotal()
    );
}}