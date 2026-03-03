package com.titan.commerce.modules.checkout.dto;

import com.titan.commerce.modules.checkout.enums.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Objeto de requisição para finalizar a compra. Os itens são resgatados automaticamente do carrinho ativo do usuário.")
public record CheckoutRequestDTO(

        @Schema(description = "ID do endereço de entrega previamente cadastrado pelo usuário", example = "89")
        @NotNull(message = "O ID do endereço é obrigatório")
        Long addressId,

        @Schema(description = "Método de pagamento escolhido pelo cliente", example = "PIX")
        @NotNull(message = "O método de pagamento é obrigatório")
        PaymentMethod paymentMethod
) {}