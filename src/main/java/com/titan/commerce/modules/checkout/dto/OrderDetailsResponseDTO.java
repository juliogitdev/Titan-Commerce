package com.titan.commerce.modules.checkout.dto;

import com.titan.commerce.modules.checkout.domain.Order;
import com.titan.commerce.modules.checkout.enums.StatusOrder;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "Visão consolidada e detalhada de uma compra")
public record OrderDetailsResponseDTO(

        @Schema(description = "ID único ou código do Pedido", example = "ORD-2026-8A7B9C")
        String orderId,

        @Schema(description = "Data e hora exata em que a compra foi realizada")
        LocalDateTime createdAt,

        @Schema(description = "Status geral do pedido", example = "PAGAMENTO_APROVADO")
        StatusOrder status,

        @Schema(description = "Valor total final do pedido", example = "3599.99")
        BigDecimal totalAmount,

        @Schema(description = "Lista de todos os produtos comprados")
        List<OrderItemResponseDTO> items,

        @Schema(description = "Histórico de pagamentos (tentativas falhas e pagamentos aprovados)")
        List<PaymentInfoResponseDTO> payments
) {

    public OrderDetailsResponseDTO(Order order) {
        this(
                order.getId(),
                order.getCreatedAt(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getItems().stream()
                        .map(OrderItemResponseDTO::new)
                        .collect(Collectors.toList()),
                order.getPayments().stream()
                        .map(PaymentInfoResponseDTO::new)
                        .collect(Collectors.toList())
        );
    }

}