package com.titan.commerce.modules.checkout.dto;

import com.titan.commerce.modules.checkout.enums.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CheckoutRequestDTO(
        @NotNull(message = "O ID do endereço é obrigatório")
        Long addressId,

        @NotNull(message = "O método de pagamento é obrigatório")
        PaymentMethod paymentMethod
)
{}
