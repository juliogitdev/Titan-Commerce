package com.titan.commerce.modules.user.controller;

import com.titan.commerce.modules.user.dto.AddressRequestDTO;
import com.titan.commerce.modules.user.dto.AddressResponseDTO;
import com.titan.commerce.modules.user.service.AddressService;
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
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
@Tag(name = "Endereços do Usuário", description = "Gerenciamento de locais de entrega para as contas de usuário")
public class AddressController {

    private final AddressService service;

    @Operation(summary = "Cadastrar novo endereço",
            description = "Adiciona um endereço ao perfil do usuário. **Atenção:** O `userId` da rota deve ser exatamente o mesmo do token JWT enviado na requisição.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Endereço cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados do endereço inválidos"),
            @ApiResponse(responseCode = "403", description = "Acesso negado: O usuário tentou adicionar um endereço em uma conta diferente da sua")
    })
    @PostMapping("/user/{userId}")
    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity<AddressResponseDTO> create(
            @Parameter(description = "ID interno do usuário", example = "1") @PathVariable Long userId,
            @RequestBody @Valid AddressRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(userId, dto));
    }

    @Operation(summary = "Listar endereços do usuário",
            description = "Retorna todos os endereços salvos de um usuário específico. **Atenção:** Requer token JWT do próprio usuário.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de endereços retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/user/{userId}")
    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity<List<AddressResponseDTO>> list(
            @Parameter(description = "ID do usuário logado", example = "1") @PathVariable Long userId) {
        return ResponseEntity.ok(service.listAllByUser(userId));
    }

    @Operation(summary = "Excluir endereço",
            description = "Remove um endereço específico da carteira do usuário. **Atenção:** Requer token JWT do próprio usuário.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Endereço removido com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "404", description = "Endereço ou Usuário não encontrados")
    })
    @DeleteMapping("/{addressId}/user/{userId}")
    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do endereço a ser apagado", example = "89") @PathVariable Long addressId,
            @Parameter(description = "ID do usuário dono do endereço", example = "1") @PathVariable Long userId) {
        service.delete(addressId, userId);
        return ResponseEntity.noContent().build();
    }
}