package com.titan.commerce.modules.catalog.service;


import com.titan.commerce.modules.catalog.domain.Product;
import com.titan.commerce.modules.catalog.domain.ProductVariant;
import com.titan.commerce.modules.catalog.dto.product.ProductResponseDTO;
import com.titan.commerce.modules.catalog.dto.productVariant.ProductVariantRequestDTO;
import com.titan.commerce.modules.catalog.dto.productVariant.ProductVariantResponseDTO;
import com.titan.commerce.modules.catalog.repository.ProductRepository;
import com.titan.commerce.modules.catalog.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductVariantService {

    private final ProductVariantRepository repository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<ProductVariantResponseDTO> findAll(Boolean active){

        List<ProductVariant> productsVariant;

        if(active == null || active){
            productsVariant = repository.findByActiveTrue();
        }else{
            productsVariant = repository.findByActiveFalse();
        }

        return productsVariant.stream().map(ProductVariantResponseDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductVariantResponseDTO findById(Long id){
        ProductVariant productVariant = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Variação de produto não encontrada"));

        return new ProductVariantResponseDTO(productVariant);
    }

    //lista todas as variantes de um produto
    @Transactional(readOnly = true)
    public List<ProductVariantResponseDTO> findByProductId(Long productId){
        if(!productRepository.existsById(productId)){
            throw new IllegalArgumentException("ID do produto inválido");
        }

        List<ProductVariant> productsVariant = repository.findByProductIdAndActiveTrue(productId);

        return productsVariant.stream().map(ProductVariantResponseDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductVariantResponseDTO findBySkuCode(String skuCode){
        ProductVariant productVariant = repository.findBySkuCode(skuCode)
                .orElseThrow(() -> new IllegalArgumentException("Código SKU não encontrado"));

        return new ProductVariantResponseDTO(productVariant);
    }

    @Transactional
    public ProductVariantResponseDTO create(ProductVariantRequestDTO requestDTO){
        if(repository.existsBySkuCode(requestDTO.getSkuCode())){
            throw new IllegalArgumentException("SKU já está cadastrado");
        }

        Product product = productRepository.findById(requestDTO.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        ProductVariant productVariant = requestDTO.toEntity();
        productVariant.setProduct(product);


        return new ProductVariantResponseDTO(repository.save(productVariant));
    }

    @Transactional
    public ProductVariantResponseDTO update(Long id, ProductVariantRequestDTO dto){
        ProductVariant variant = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Variante não encontrada"));

        // Se o SKU mudou E o novo SKU já existe no banco -> Erro
        if (!variant.getSkuCode().equals(dto.getSkuCode()) && repository.existsBySkuCode(dto.getSkuCode())) {
            throw new IllegalArgumentException("Já existe outra variante com o SKU: " + dto.getSkuCode());
        }

        variant.setSkuCode(dto.getSkuCode());
        variant.setPrice(dto.getPrice());
        variant.setStockQuantity(dto.getStockQuantity());
        variant.setAttributes(dto.getAttributes());



        repository.save(variant);
        return new ProductVariantResponseDTO(variant);
    }

    @Transactional
    public ProductVariantResponseDTO updateStock(Long id, Integer quantity){
        ProductVariant variant = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID da variante inválida"));

        if(quantity < 0){
            throw new IllegalArgumentException("A quantidade no estoque não pode ser menor que 0");
        }

        variant.setStockQuantity(quantity);

        return new ProductVariantResponseDTO(repository.save(variant));
    }

    @Transactional
    public ProductVariantResponseDTO adjustStock(Long id, Integer quantity) {

        ProductVariant variant = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido"));

        int newStock = variant.getStockQuantity() + quantity;

        if (newStock < 0) {
            throw new IllegalArgumentException("Estoque insuficiente para essa operação");
        }

        variant.setStockQuantity(newStock);

        return new ProductVariantResponseDTO(repository.save(variant));
    }

    @Transactional
    public void delete(Long id) {
        ProductVariant variant = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Variante não encontrada"));

        variant.setActive(false);
        repository.save(variant);
    }

    @Transactional
    public void activate(Long id) {
        ProductVariant variant = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Variante não encontrada"));

        variant.setActive(true);
        repository.save(variant);
    }
}
