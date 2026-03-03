package com.titan.commerce.modules.catalog.controller;

import com.titan.commerce.modules.catalog.dto.productVariant.ProductVariantRequestDTO;
import com.titan.commerce.modules.catalog.dto.productVariant.ProductVariantResponseDTO;
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
@RequestMapping("/api/product-variants")
@Tag(name = "Variantes de Produto", description = "Gerenciamento de SKUs, preços específicos e controle de estoque")
public class ProductVariantController {

    private final ProductVariantService service;

    @Operation(summary = "Listar variantes", description = "Retorna todas as variantes cadastradas, com opção de filtrar por status ativo/inativo.")
    @GetMapping
    public ResponseEntity<List<ProductVariantResponseDTO>> listar(
            @Parameter(description = "Filtro de status (true = ativo, false = inativo)")
            @RequestParam(required = false) Boolean active){
        return ResponseEntity.ok(service.findAll(active));
    }

    @Operation(summary = "Criar nova variante", description = "Adiciona um novo SKU (tamanho, cor, etc.) a um produto base existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Variante criada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto base não encontrado")
    })
    @PostMapping
    public ResponseEntity<ProductVariantResponseDTO> criar(@RequestBody ProductVariantRequestDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @Operation(summary = "Apagar/Desativar variante", description = "Desativa uma variante específica do catálogo.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Variante apagada/desativada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Variante não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@Parameter(description = "ID da variante") @PathVariable Long id){
        if (service.delete(id)){
            return ResponseEntity.ok("Variante apagada com sucesso");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Variante não encontrada");
    }

    @Operation(summary = "Atualizar variante", description = "Sobrescreve os dados de uma variante específica.")
    @PutMapping("/{id}")
    public ResponseEntity<ProductVariantResponseDTO> update(
            @Parameter(description = "ID da variante") @PathVariable Long id,
            @RequestBody ProductVariantRequestDTO requestDTO) {
        ProductVariantResponseDTO productVariantUpdated = service.update(id, requestDTO);
        return ResponseEntity.ok(productVariantUpdated);
    }

    @Operation(summary = "Buscar por ID", description = "Retorna os detalhes de uma variante pelo seu ID interno.")
    @GetMapping("/{id}")
    public ResponseEntity<ProductVariantResponseDTO> findById(@Parameter(description = "ID da variante") @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Buscar por SKU", description = "Busca uma variante através do seu código único de estoque (SKU). Útil para integrações.")
    @GetMapping("/sku/{skuCode}")
    public ResponseEntity<ProductVariantResponseDTO> findBySkuCode(@Parameter(description = "Código SKU da variante") @PathVariable String skuCode) {
        return ResponseEntity.ok(service.findBySkuCode(skuCode));
    }

    @Operation(summary = "Definir estoque absoluto", description = "Substitui a quantidade atual de estoque pelo valor exato informado (ex: recontagem de inventário).")
    @PatchMapping("/{id}/update-stock")
    public ResponseEntity<ProductVariantResponseDTO> updateStock(
            @Parameter(description = "ID da variante") @PathVariable Long id,
            @Parameter(description = "Novo valor total do estoque", example = "100") @RequestParam Integer quantity) {
        return ResponseEntity.ok(service.updateStock(id, quantity));
    }

    @Operation(summary = "Ajustar estoque (+/-)", description = "Adiciona ou subtrai a quantidade informada do estoque atual (ex: +10 para nova remessa, -1 para venda).")
    @PatchMapping("/{id}/adjust-stock")
    public ResponseEntity<ProductVariantResponseDTO> adjustStock(
            @Parameter(description = "ID da variante") @PathVariable Long id,
            @Parameter(description = "Valor a ser somado ou subtraído", example = "-2") @RequestParam Integer quantity) {
        return ResponseEntity.ok(service.adjustStock(id, quantity));
    }

    @Operation(summary = "Ativar variante", description = "Torna a variante visível e disponível para venda novamente.")
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@Parameter(description = "ID da variante") @PathVariable Long id) {
        service.activate(id);
        return ResponseEntity.noContent().build();
    }
}