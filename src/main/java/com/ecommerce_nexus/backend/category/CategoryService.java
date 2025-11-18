package com.ecommerce_nexus.backend.category;

import com.ecommerce_nexus.backend.category.dto.CategoryRequest;
import com.ecommerce_nexus.backend.category.dto.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository repository;

    @Transactional(readOnly = true)
    public List<CategoryResponse> findAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponse findById(Long id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + id));
    }

    public CategoryResponse create(CategoryRequest req) {
        repository.findByNameIgnoreCase(req.getName()).ifPresent(c -> {
            throw new IllegalArgumentException("Category name already exists: " + req.getName());
        });
        Category category = new Category();
        category.setName(req.getName());
        category.setCoverImage(req.getCoverImage());
        Category saved = repository.save(category);
        return toResponse(saved);
    }

    public CategoryResponse update(Long id, CategoryRequest req) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + id));
        if (!category.getName().equalsIgnoreCase(req.getName())) {
            repository.findByNameIgnoreCase(req.getName()).ifPresent(existing -> {
                if (!existing.getId().equals(id)) {
                    throw new IllegalArgumentException("Category name already exists: " + req.getName());
                }
            });
        }
        category.setName(req.getName());
        category.setCoverImage(req.getCoverImage());
        return toResponse(category);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Category not found: " + id);
        }
        repository.deleteById(id);
    }

    private CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .coverImage(category.getCoverImage())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}
