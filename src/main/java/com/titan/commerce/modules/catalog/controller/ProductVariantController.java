package com.titan.commerce.modules.catalog.controller;

import com.titan.commerce.modules.catalog.dto.productVariant.ProductVariantRequestDTO;
import com.titan.commerce.modules.catalog.dto.productVariant.ProductVariantResponseDTO;
import com.titan.commerce.modules.catalog.service.ProductVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product-variants")
public class ProductVariantController {

    private final ProductVariantService service;

    @GetMapping
    public ResponseEntity<List<ProductVariantResponseDTO>> listar(@RequestParam(required = false) Boolean active){
        return ResponseEntity.ok(service.findAll(active));
    }

    @PostMapping
    public ResponseEntity<ProductVariantResponseDTO> criar(@RequestBody ProductVariantRequestDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        if (service.delete(id)){
            return ResponseEntity.ok("Variante apagada com sucesso");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Variante não encontrada");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductVariantResponseDTO> update(@PathVariable Long id, @RequestBody ProductVariantRequestDTO requestDTO) {
        ProductVariantResponseDTO productVariantUpdated = service.update(id, requestDTO);
        return ResponseEntity.ok(productVariantUpdated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductVariantResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/sku/{skuCode}")
    public ResponseEntity<ProductVariantResponseDTO> findBySkuCode(@PathVariable String skuCode) {
        return ResponseEntity.ok(service.findBySkuCode(skuCode));
    }

    @PatchMapping("/{id}/update-stock")
    public ResponseEntity<ProductVariantResponseDTO> updateStock(@PathVariable Long id, @RequestParam Integer quantity) {
        return ResponseEntity.ok(service.updateStock(id, quantity));
    }

    @PatchMapping("/{id}/adjust-stock")
    public ResponseEntity<ProductVariantResponseDTO> adjustStock(@PathVariable Long id, @RequestParam Integer quantity) {
        return ResponseEntity.ok(service.adjustStock(id, quantity));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable Long id) {
        service.activate(id);
        return ResponseEntity.noContent().build();
    }

}
