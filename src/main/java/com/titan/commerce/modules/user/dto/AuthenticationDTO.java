package com.titan.commerce.modules.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationDTO {

    @Schema(description = "E-mail cadastrado do usuário", example = "cliente@ecommerce.com.br")
    @NotBlank
    @Email
    private String email;

    @Schema(description = "Senha de acesso", example = "SenhaSegura123!")
    @NotBlank
    private String password;
}