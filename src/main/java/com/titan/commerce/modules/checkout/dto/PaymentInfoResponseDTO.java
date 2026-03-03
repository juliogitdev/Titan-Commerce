package com.titan.commerce.modules.checkout.dto;

import com.titan.commerce.modules.checkout.domain.Payment;
import com.titan.commerce.modules.checkout.enums.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Informações sobre a tentativa ou confirmação de pagamento")
public record PaymentInfoResponseDTO(

        @Schema(description = "ID do pagamento gerado no Gateway", example = "pay_987654321")
        String id,

        @Schema(description = "Método de pagamento utilizado", example = "PIX")
        PaymentMethod method,

        @Schema(description = "Status atual deste pagamento", example = "APROVADO")
        String status, // Pode ser um Enum se você tiver um PaymentStatus

        @Schema(description = "Data e hora em que esta tentativa foi registrada")
        LocalDateTime processedAt
) {

    public PaymentInfoResponseDTO(Payment payment) {
        this(
                payment.getId(),
                payment.getPaymentMethod(),
                payment.getStatus().name(),
                payment.getPaidAt()
        );
    }

}