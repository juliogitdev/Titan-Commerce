package com.titan.commerce.modules.checkout.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CheckoutItemDTO(
        @NotNull Long productVariantId,
        @Min(1) Integer quantity
)
{
}
