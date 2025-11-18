package com.ecommerce_nexus.backend.review;

import com.ecommerce_nexus.backend.product.Product;
import com.ecommerce_nexus.backend.product.ProductRepository;
import com.ecommerce_nexus.backend.review.dto.ReviewRequest;
import com.ecommerce_nexus.backend.review.dto.ReviewResponse;
import com.ecommerce_nexus.backend.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository repository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<ReviewResponse> getByProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));
        return repository.findByProductOrderByCreatedAtDesc(product).stream().map(this::toResponse).toList();
    }

    public ReviewResponse createOrUpdate(ReviewRequest req) {
        User user = getCurrentUser();
        Product product = productRepository.findById(req.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + req.getProductId()));
        Review review = repository.findByUserAndProduct(user, product).orElseGet(() -> {
            Review r = new Review();
            r.setUser(user);
            r.setProduct(product);
            return r;
        });
        review.setRating(req.getRating());
        review.setComment(req.getComment());
        return toResponse(repository.save(review));
    }

    public void delete(Long id) {
        Review review = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Review not found: " + id));
        if (!review.getUser().getId().equals(getCurrentUser().getId())) {
            throw new IllegalArgumentException("Cannot delete other user's review");
        }
        repository.delete(review);
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private ReviewResponse toResponse(Review r) {
        return ReviewResponse.builder()
                .id(r.getId())
                .productId(r.getProduct().getId())
                .userId(r.getUser().getId())
                .userEmail(r.getUser().getEmail())
                .rating(r.getRating())
                .comment(r.getComment())
                .createdAt(r.getCreatedAt())
                .updatedAt(r.getUpdatedAt())
                .build();
    }
}
