package com.titan.commerce.modules.checkout.dto;

import com.titan.commerce.modules.checkout.enums.StatusOrder;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Resumo da ordem de pedido gerada após o checkout")
public record CheckoutResponseDTO(

        @Schema(description = "Código identificador único do Pedido", example = "ORD-2026-8A7B9C")
        String orderId,

        @Schema(description = "Status atual do pedido", example = "AGUARDANDO_PAGAMENTO")
        StatusOrder status,

        @Schema(description = "Valor total final da compra", example = "3599.99")
        BigDecimal totalAmount,

        @Schema(description = "Data limite para o pagamento (ex: vencimento do PIX ou Boleto)")
        LocalDateTime expiresAt,

        @Schema(description = "ID de referência do pagamento gerado no gateway", example = "pay_987654321")
        String paymentId
) {}