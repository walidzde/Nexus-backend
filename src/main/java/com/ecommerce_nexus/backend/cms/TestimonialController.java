package com.ecommerce_nexus.backend.cms;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cms/testimonials")
@RequiredArgsConstructor
public class TestimonialController {

    private final TestimonialRepository repository;

    @GetMapping
    public ResponseEntity<List<Testimonial>> getActive() {
        return ResponseEntity.ok(repository.findByActiveTrueOrderBySortOrderAsc());
    }

    @PostMapping
    public ResponseEntity<Testimonial> create(@RequestBody Testimonial t) {
        return ResponseEntity.ok(repository.save(t));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Testimonial> update(@PathVariable Long id, @RequestBody Testimonial t) {
        Testimonial existing = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Testimonial not found: " + id));
        existing.setAuthor(t.getAuthor());
        existing.setContent(t.getContent());
        existing.setActive(t.isActive());
        existing.setSortOrder(t.getSortOrder());
        return ResponseEntity.ok(repository.save(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
