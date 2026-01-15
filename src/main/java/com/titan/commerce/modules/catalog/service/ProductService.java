package com.titan.commerce.modules.catalog.service;

import com.titan.commerce.modules.catalog.domain.Category;
import com.titan.commerce.modules.catalog.domain.Product;
import com.titan.commerce.modules.catalog.dto.product.ProductRequestDTO;
import com.titan.commerce.modules.catalog.dto.product.ProductResponseDTO;
import com.titan.commerce.modules.catalog.repository.CategoryRepository;
import com.titan.commerce.modules.catalog.repository.ProductRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findAll(Boolean active){
        List<Product> products;

        if(active == null || active){
            products = repository.findByActiveTrue();
        }else{
            products = repository.findByActiveFalse();
        }

        return products
                .stream()
                .map(ProductResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductResponseDTO create(ProductRequestDTO productRequestDTO){

        //verifica se já foi cadastrado aquele titulo antes
        if(repository.existsByTitle(productRequestDTO.getTitle())){
            throw new IllegalArgumentException("já existe um produto com esse nome");
        }

        Product newProduct = productRequestDTO.toEntity();

        if (productRequestDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(productRequestDTO.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada"));

            if (!Boolean.TRUE.equals(category.getActive())) { // Null-safe check
                throw new IllegalArgumentException("Categoria desativada");
            }

            newProduct.setCategory(category);
        }

        repository.save(newProduct);
        return new ProductResponseDTO(newProduct);

    }

    @Transactional(readOnly = true)
    public ProductResponseDTO findById(Long id){
        Product product = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        return new ProductResponseDTO(product);
    }

    @Transactional
    public Boolean delete(Long id){
        if(repository.existsById(id)){
            Product product = repository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

            product.setActive(false);
            repository.save(product);
            return true;
        }

        return false;
    }

    @Transactional
    public ProductResponseDTO activate  (Long id){
        Product product = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("falha ao buscar produto"));

        if(Boolean.TRUE.equals(product.getActive())){
            throw new IllegalArgumentException("Este produto já está ativado");
        }

        product.setActive(true);

        return new ProductResponseDTO(repository.save(product));



    }

    @Transactional
    public ProductResponseDTO update(Long id, ProductRequestDTO dtoRequest){
        Product product = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        if (dtoRequest.getTitle() != null && !dtoRequest.getTitle().equals(product.getTitle())) {
            if (repository.existsByTitle(dtoRequest.getTitle())) {
                throw new IllegalArgumentException("Já existe um produto com esse nome");
            }
            product.setTitle(dtoRequest.getTitle());
        }

        if (dtoRequest.getDescription() != null){
            product.setDescription(dtoRequest.getDescription());
        }
        if (dtoRequest.getBrand() != null) {
            product.setBrand(dtoRequest.getBrand());
        }
        if (dtoRequest.getActive() != null){
            product.setActive(dtoRequest.getActive());
        }

        if (dtoRequest.getCategoryId() != null){
            Category category = categoryRepository.findById(dtoRequest.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada"));

            if (!Boolean.TRUE.equals(category.getActive())) {
                throw new IllegalArgumentException("Categoria desativada");
            }

            product.setCategory(category);
        }

        Product productUpdated = repository.save(product);

        return new ProductResponseDTO(productUpdated);

    }

}
