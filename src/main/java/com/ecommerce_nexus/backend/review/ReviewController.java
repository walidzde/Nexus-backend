package com.ecommerce_nexus.backend.review;

import com.ecommerce_nexus.backend.review.dto.ReviewRequest;
import com.ecommerce_nexus.backend.review.dto.ReviewResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService service;

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewResponse>> getByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(service.getByProduct(productId));
    }

    @PostMapping
    public ResponseEntity<ReviewResponse> createOrUpdate(@Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.ok(service.createOrUpdate(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
