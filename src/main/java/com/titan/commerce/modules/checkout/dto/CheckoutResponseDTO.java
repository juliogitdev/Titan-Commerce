package com.titan.commerce.modules.checkout.dto;

import com.titan.commerce.modules.checkout.enums.StatusOrder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CheckoutResponseDTO(
        String orderId,
        StatusOrder status,
        BigDecimal totalAmount,
        LocalDateTime expiresAt,
        String paymentId
) {
}