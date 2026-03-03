package com.titan.commerce.modules.catalog.controller;

import com.titan.commerce.modules.catalog.dto.product.ProductRequestDTO;
import com.titan.commerce.modules.catalog.dto.product.ProductResponseDTO;
import com.titan.commerce.modules.catalog.dto.productVariant.ProductVariantResponseDTO;
import com.titan.commerce.modules.catalog.service.ProductService;
import com.titan.commerce.modules.catalog.service.ProductVariantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
@Tag(name = "Produtos", description = "Endpoints para o gerenciamento de produtos do e-commerce")
public class ProductController {

    private final ProductService service;
    private final ProductVariantService productVariantService;

    @Operation(summary = "Listar produtos", description = "Retorna o catálogo de produtos. Pode ser filtrado por status (ativo/inativo).")
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> listar(
            @Parameter(description = "Filtro para buscar apenas produtos ativos (true) ou inativos (false)")
            @RequestParam(required = false) Boolean active){
        return ResponseEntity.ok(service.findAll(active));
    }

    @Operation(summary = "Criar novo produto", description = "Registra um novo produto base no catálogo.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados")
    })
    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@RequestBody ProductRequestDTO request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(request));
    }

    @Operation(summary = "Buscar produto por ID", description = "Obtém todos os detalhes de um produto específico através do seu ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> findById(@Parameter(description = "ID do produto") @PathVariable Long id){
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Apagar/Desativar produto", description = "Realiza o soft delete (desativação) de um produto existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto apagado (desativado) com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@Parameter(description = "ID do produto a ser desativado") @PathVariable Long id){
        if (service.delete(id)){
            return ResponseEntity.ok("Produto apagado(desativado) com sucesso");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
    }

    @Operation(summary = "Ativar produto", description = "Reativa um produto previamente desativado, tornando-o visível novamente.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Produto ativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@Parameter(description = "ID do produto a ser ativado") @PathVariable Long id) {
        service.activate(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Atualizar produto", description = "Atualiza as informações de um produto existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> update(
            @Parameter(description = "ID do produto") @PathVariable Long id,
            @RequestBody ProductRequestDTO requestDTO) {
        ProductResponseDTO productUpdated = service.update(id, requestDTO);
        return ResponseEntity.ok(productUpdated);
    }

    @Operation(summary = "Buscar variantes do produto", description = "Lista todas as variantes (ex: cores, tamanhos) de um produto específico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Variantes encontradas"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @GetMapping("/{id}/variants")
    public ResponseEntity<List<ProductVariantResponseDTO>> findByProductId(
            @Parameter(description = "ID do produto base") @PathVariable Long id) {
        return ResponseEntity.ok(productVariantService.findByProductId(id));
    }
}