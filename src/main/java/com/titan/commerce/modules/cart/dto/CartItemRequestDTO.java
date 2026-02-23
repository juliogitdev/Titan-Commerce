package com.titan.commerce.modules.cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Objeto de requisição para adicionar um item ao carrinho")
public record CartItemRequestDTO(

        @Schema(description = "ID da variante do produto que será adicionada", example = "340")
        @NotNull(message = "O ID da variante é obrigatório")
        Long productVariantId,

        @Schema(description = "Quantidade de itens a ser adicionada", example = "2")
        @NotNull(message = "A quantidade é obrigatória")
        @Min(value = 1, message = "A quantidade deve ser pelo menos 1")
        Integer quantity
) {}