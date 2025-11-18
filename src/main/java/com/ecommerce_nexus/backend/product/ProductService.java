package com.ecommerce_nexus.backend.product;

import com.ecommerce_nexus.backend.category.Category;
import com.ecommerce_nexus.backend.category.CategoryRepository;
import com.ecommerce_nexus.backend.product.dto.ProductRequest;
import com.ecommerce_nexus.backend.product.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<ProductResponse> findAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        return repository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));
    }

    public ProductResponse create(ProductRequest req) {
        Product product = new Product();
        product.setName(req.getName());
        product.setDescription(req.getDescription());
        product.setPrice(req.getPrice());
        product.setStock(req.getStock());
        product.setImageUrl(req.getImageUrl());
        if (req.getCategoryId() != null) {
            Category category = categoryRepository.findById(req.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found: " + req.getCategoryId()));
            product.setCategory(category);
        } else {
            product.setCategory(null);
        }
        Product saved = repository.save(product);
        return toResponse(saved);
    }

    public ProductResponse update(Long id, ProductRequest req) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));
        product.setName(req.getName());
        product.setDescription(req.getDescription());
        product.setPrice(req.getPrice());
        product.setStock(req.getStock());
        product.setImageUrl(req.getImageUrl());
        if (req.getCategoryId() != null) {
            Category category = categoryRepository.findById(req.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found: " + req.getCategoryId()));
            product.setCategory(category);
        } else {
            product.setCategory(null);
        }
        return toResponse(product);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Product not found: " + id);
        }
        repository.deleteById(id);
    }

    private ProductResponse toResponse(Product p) {
        return ProductResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .price(p.getPrice())
                .stock(p.getStock())
                .imageUrl(p.getImageUrl())
                .categoryId(p.getCategory() != null ? p.getCategory().getId() : null)
                .categoryName(p.getCategory() != null ? p.getCategory().getName() : null)
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();
    }
}
