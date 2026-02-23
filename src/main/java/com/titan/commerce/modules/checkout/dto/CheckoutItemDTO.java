package com.titan.commerce.modules.checkout.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Representação de um item no momento do checkout")
public record CheckoutItemDTO(

        @Schema(description = "ID da variante do produto", example = "340")
        @NotNull
        Long productVariantId,

        @Schema(description = "Quantidade comprada", example = "1")
        @Min(1)
        Integer quantity
) {}