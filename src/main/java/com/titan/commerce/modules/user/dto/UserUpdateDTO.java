package com.titan.commerce.modules.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserUpdateDTO {

    @Schema(description = "Novo nome do usuário", example = "Maria da Silva Costa")
    private String name;

    @Schema(description = "Novo e-mail do usuário", example = "maria.costa@email.com")
    @Email
    private String email;
}