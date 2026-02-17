package com.titan.commerce.modules.checkout.controller;

import com.titan.commerce.modules.checkout.domain.Order;
import com.titan.commerce.modules.checkout.dto.CheckoutRequestDTO;
import com.titan.commerce.modules.checkout.dto.CheckoutResponseDTO;
import com.titan.commerce.modules.checkout.service.CheckoutService;
import com.titan.commerce.modules.user.domain.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    @PostMapping()
    public ResponseEntity<CheckoutResponseDTO> processCheckout(
            @AuthenticationPrincipal User user,
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
