package com.titan.commerce.modules.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {

    @Schema(description = "Nome completo do usuário", example = "Maria Silva")
    @NotBlank
    private String name;

    @Schema(description = "E-mail válido que será usado para login", example = "maria.silva@email.com")
    @NotBlank
    @Email
    private String email;

    @Schema(description = "Senha de acesso (mínimo 8 caracteres)", example = "SenhaForte@2024")
    @NotBlank
    @Size(min = 8)
    private String password;
}