package com.titan.commerce.modules.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PasswordUpdateDTO {

    @Schema(description = "Senha atual do usuário para validação de segurança", example = "SenhaAntiga123")
    @NotBlank
    private String currentPassword;

    @Schema(description = "Nova senha desejada (mínimo 8 caracteres)", example = "NovaSenhaForte!99")
    @NotBlank
    @Size(min = 8)
    private String newPassword;
}