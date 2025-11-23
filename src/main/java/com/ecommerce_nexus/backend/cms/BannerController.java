package com.ecommerce_nexus.backend.cms;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cms/banners")
@RequiredArgsConstructor
public class BannerController {

    private final BannerRepository repository;

    @GetMapping
    public ResponseEntity<List<Banner>> getActive() {
        return ResponseEntity.ok(repository.findByActiveTrueOrderBySortOrderAsc());
    }

    @PostMapping
    public ResponseEntity<Banner> create(@RequestBody Banner banner) {
        return ResponseEntity.ok(repository.save(banner));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Banner> update(@PathVariable Long id, @RequestBody Banner banner) {
        Banner b = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Banner not found: " + id));
        b.setTitle(banner.getTitle());
        b.setImageUrl(banner.getImageUrl());
        b.setLinkUrl(banner.getLinkUrl());
        b.setActive(banner.isActive());
        b.setSortOrder(banner.getSortOrder());
        return ResponseEntity.ok(repository.save(b));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
