package com.titan.commerce.modules.user.dto;

import com.titan.commerce.modules.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UserResponseDTO {

    @Schema(description = "ID único do usuário", example = "1")
    private Long id;

    @Schema(description = "Nome completo", example = "Maria Silva")
    private String name;

    @Schema(description = "E-mail de cadastro", example = "maria.silva@email.com")
    private String email;

    @Schema(description = "Status da conta (true = ativa, false = bloqueada/inativa)", example = "true")
    private Boolean active;

    @Schema(description = "Data e hora do registro no sistema")
    private LocalDateTime createdAt;

    @Schema(description = "Data e hora da última alteração de dados")
    private LocalDateTime updatedAt;

    public UserResponseDTO(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.active = user.isActive();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }
}