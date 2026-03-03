package com.titan.commerce.modules.user.controller;

import com.titan.commerce.modules.user.dto.PasswordUpdateDTO;
import com.titan.commerce.modules.user.dto.UserResponseDTO;
import com.titan.commerce.modules.user.dto.UserUpdateDTO;
import com.titan.commerce.modules.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Gestão de Usuários", description = "Endpoints para administração de contas, perfis e credenciais")
public class UserController {

    private final UserService service;

    @Operation(summary = "Buscar usuário por ID",
            description = "Retorna os detalhes de um usuário. **Segurança:** Acesso permitido apenas para Administradores ou para o próprio dono da conta.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado: Você não tem permissão para ver os dados de outro usuário"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UserResponseDTO> findById(@Parameter(description = "ID do usuário") @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Listar todos os usuários",
            description = "Retorna uma lista completa de usuários do sistema, com filtro opcional de status. **Segurança:** Requer privilégios de ADMIN.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> findAll(
            @Parameter(description = "Filtrar por contas ativas (true) ou inativas (false)") @RequestParam(required = false) Boolean active) {
        return ResponseEntity.ok(service.findAll(active));
    }

    @Operation(summary = "Atualizar dados do perfil",
            description = "Altera informações básicas como nome e e-mail. **Segurança:** Acesso restrito ao próprio dono da conta.")
    @ApiResponse(responseCode = "200", description = "Perfil atualizado com sucesso")
    @PutMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id")
    public ResponseEntity<UserResponseDTO> update(
            @Parameter(description = "ID do usuário (deve bater com o Token JWT)") @PathVariable Long id,
            @Valid @RequestBody UserUpdateDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Apagar/Bloquear usuário",
            description = "Remove o acesso de um usuário ao sistema. **Segurança:** Requer privilégios de ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@Parameter(description = "ID do usuário a ser removido") @PathVariable Long id) {
        if (service.delete(id)){
            return ResponseEntity.ok().body("Usuário removido com sucesso");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
    }

    @Operation(summary = "Reativar usuário",
            description = "Restaura o acesso de uma conta previamente desativada. **Segurança:** Requer privilégios de ADMIN.")
    @ApiResponse(responseCode = "204", description = "Usuário ativado com sucesso")
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> patch(@Parameter(description = "ID do usuário") @PathVariable Long id) {
        service.activate(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Atualizar senha",
            description = "Altera a senha de acesso da conta. **Segurança:** Acesso restrito ao próprio dono da conta.")
    @ApiResponse(responseCode = "200", description = "Senha atualizada com sucesso")
    @PatchMapping("/{id}/password")
    @PreAuthorize("#id == authentication.principal.id")
    public ResponseEntity<String> passwordUpdate(
            @Parameter(description = "ID do usuário (deve bater com o Token JWT)") @PathVariable Long id,
            @Valid @RequestBody PasswordUpdateDTO dto) {
        service.passwordUpdate(id, dto);
        return ResponseEntity.ok("Senha atualizada com sucesso");
    }
}