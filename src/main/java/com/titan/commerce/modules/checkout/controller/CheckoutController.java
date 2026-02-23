package com.titan.commerce.modules.checkout.controller;

import com.titan.commerce.modules.checkout.domain.Order;
import com.titan.commerce.modules.checkout.dto.CheckoutRequestDTO;
import com.titan.commerce.modules.checkout.dto.CheckoutResponseDTO;
import com.titan.commerce.modules.checkout.service.CheckoutService;
import com.titan.commerce.modules.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/checkout")
@Tag(name = "Checkout e Pedidos", description = "Processamento de finalização de compras e geração de ordens de pagamento")
public class CheckoutController {

    private final CheckoutService checkoutService;

    @Operation(summary = "Processar Checkout (Finalizar Compra)",
            description = "Converte o carrinho ativo do usuário logado em um Pedido (Order) oficial. Vincula o endereço de entrega e gera a intenção de pagamento no método escolhido.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Checkout processado com sucesso. Pedido criado."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos, carrinho vazio ou produto sem estoque"),
            @ApiResponse(responseCode = "404", description = "Endereço fornecido não encontrado para este usuário")
    })
    @PostMapping
    public ResponseEntity<CheckoutResponseDTO> processCheckout(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @RequestBody @Valid CheckoutRequestDTO request)
    {
        // 1. Processa a compra
        Order order = checkoutService.processCheckout(user, request);

        // 2. Mapeia para o DTO de resposta
        String paymentId = order.getPayments().isEmpty() ? null : order.getPayments().get(0).getId();

        CheckoutResponseDTO response = new CheckoutResponseDTO(
                order.getId(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getExpiresAt(),
                paymentId
        );

        return ResponseEntity.ok().body(response);
    }
}