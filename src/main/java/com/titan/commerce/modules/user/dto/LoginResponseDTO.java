package com.titan.commerce.modules.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {

    @Schema(description = "Token JWT que deve ser enviado no cabeçalho (Authorization: Bearer) das próximas requisições",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJjbGllbnRlQGVjb21tZXJjZS5jb20uYnIiLCJpYXQiOjE3MDkxOTM2MDB9.ExemploDeAssinaturaJWT")
    private String token;
}