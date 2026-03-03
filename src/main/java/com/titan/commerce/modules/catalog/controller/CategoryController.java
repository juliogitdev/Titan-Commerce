package com.titan.commerce.modules.catalog.controller;

import com.titan.commerce.modules.catalog.domain.Category;
import com.titan.commerce.modules.catalog.dto.category.CategoryRequestDTO;
import com.titan.commerce.modules.catalog.dto.category.CategoryResponseDTO;
import com.titan.commerce.modules.catalog.dto.product.ProductResponseDTO;
import com.titan.commerce.modules.catalog.service.CategoryService;
import com.titan.commerce.modules.catalog.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Categorias", description = "Endpoints para gerenciamento do catálogo de categorias e subcategorias")
public class CategoryController {

    private final CategoryService service;
    private final ProductService productService;

    @Operation(summary = "Listar todas as categorias", description = "Retorna uma lista de categorias. Pode ser filtrada pelo status de ativação.")
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> listar(
            @Parameter(description = "Filtrar por status (true para ativas, false para inativas)")
            @RequestParam(required = false) Boolean active) {
        return ResponseEntity.ok(service.findAll(active));
    }

    @Operation(summary = "Criar nova categoria", description = "Cria uma nova categoria ou subcategoria no sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos enviados na requisição")
    })
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> criar(@RequestBody @Valid CategoryRequestDTO categoryDTO){
        CategoryResponseDTO newCategoryResponseDto = service.create(categoryDTO);
        return ResponseEntity.status(201).body(newCategoryResponseDto);
    }

    @Operation(summary = "Deletar categoria", description = "Remove uma categoria existente pelo seu ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@Parameter(description = "ID da categoria") @PathVariable Long id){
        if(service.delete(id)){
            return ResponseEntity.ok().body("Categoria removida com sucesso");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categoria não foi encontrada");
    }

    @Operation(summary = "Buscar categoria por ID", description = "Retorna os detalhes de uma categoria específica.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria encontrada"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> findById(@Parameter(description = "ID da categoria") @PathVariable Long id){
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Buscar subcategorias", description = "Retorna todas as subcategorias filhas de uma categoria pai.")
    @GetMapping("/{id}/subcategories")
    public ResponseEntity<List<CategoryResponseDTO>> findByParentId(@Parameter(description = "ID da categoria pai") @PathVariable Long id){
        return ResponseEntity.ok(service.findByChildrens(id));
    }

    @Operation(summary = "Atualizar categoria", description = "Atualiza os dados de uma categoria existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> update(
            @Parameter(description = "ID da categoria a ser atualizada") @PathVariable @Valid Long id,
            @RequestBody CategoryRequestDTO requestDTO){
        CategoryResponseDTO categoriaUpdated = service.update(id, requestDTO);
        return ResponseEntity.ok(categoriaUpdated);
    }

    @Operation(summary = "Ativar categoria", description = "Muda o status da categoria para ativa.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Categoria ativada com sucesso (sem conteúdo no retorno)"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@Parameter(description = "ID da categoria") @PathVariable Long id){
        service.activate(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar produtos da categoria", description = "Retorna todos os produtos associados a uma categoria específica.")
    @GetMapping("/{id}/products")
    public ResponseEntity<List<ProductResponseDTO>> findByCategoryId(@Parameter(description = "ID da categoria") @PathVariable Long id) {
        return ResponseEntity.ok(productService.findByCategory(id));
    }
}