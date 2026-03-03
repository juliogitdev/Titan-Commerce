package com.titan.commerce.modules.checkout.controller;

import com.titan.commerce.modules.checkout.dto.OrderDetailsResponseDTO;
import com.titan.commerce.modules.checkout.service.OrderService;
import com.titan.commerce.modules.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@Tag(name = "Pedidos (Orders)", description = "Gerenciamento, histórico e acompanhamento de pedidos finalizados")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Listar minhas compras",
            description = "Retorna o histórico completo de pedidos do usuário logado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Histórico de compras retornado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado (Token ausente ou inválido)")
    })
    @GetMapping("/me")
    public ResponseEntity<List<OrderDetailsResponseDTO>> getMyOrders(
            @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(orderService.findOrdersByUser(user.getId()));
    }

    @Operation(summary = "Buscar detalhes de um pedido",
            description = "Retorna os detalhes completos de um pedido específico. O usuário só pode visualizar o pedido se for o dono dele.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Detalhes do pedido retornados com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado: O pedido pertence a outro usuário"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailsResponseDTO> getOrderById(
            @Parameter(description = "ID do Pedido") @PathVariable String orderId,
            @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        // O Service deve validar se o pedido realmente pertence ao user.getId() antes de retornar!
        return ResponseEntity.ok(orderService.findOrderByIdAndUser(orderId, user.getId()));
    }

    @Operation(summary = "Listar todas as compras (Admin)",
            description = "Retorna o histórico geral de todas as vendas da plataforma. Requer privilégio de Administrador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista geral retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado: Requer privilégio de ADMIN")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderDetailsResponseDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.findAllOrders());
    }
}