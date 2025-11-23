package com.ecommerce_nexus.backend.category;

import com.ecommerce_nexus.backend.category.dto.CategoryRequest;
import com.ecommerce_nexus.backend.category.dto.CategoryResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock private CategoryRepository repository;
    @InjectMocks private CategoryService service;

    @Test
    void findAll_mapsEntitiesToResponses() {
        Category c1 = Category.builder().id(1L).name("A").coverImage("img").build();
        c1.onCreate();
        Category c2 = Category.builder().id(2L).name("B").build();
        c2.onCreate();
        when(repository.findAll()).thenReturn(List.of(c1, c2));

        List<CategoryResponse> res = service.findAll();
        assertEquals(2, res.size());
        assertEquals("A", res.get(0).getName());
        assertNotNull(res.get(0).getCreatedAt());
    }

    @Test
    void findById_returnsMappedResponse_whenExists() {
        Category c = Category.builder().id(10L).name("Shoes").build();
        c.onCreate();
        when(repository.findById(10L)).thenReturn(Optional.of(c));

        CategoryResponse resp = service.findById(10L);
        assertEquals(10L, resp.getId());
        assertEquals("Shoes", resp.getName());
    }

    @Test
    void findById_throws_whenMissing() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.findById(99L));
    }

    @Test
    void create_savesNewCategory_whenNameUnique() {
        CategoryRequest req = new CategoryRequest();
        req.setName("Electronics");
        req.setCoverImage("img");

        when(repository.findByNameIgnoreCase("Electronics")).thenReturn(Optional.empty());
        when(repository.save(any(Category.class))).thenAnswer(inv -> {
            Category c = inv.getArgument(0);
            c.setId(5L);
            c.onCreate();
            return c;
        });

        CategoryResponse resp = service.create(req);
        assertEquals(5L, resp.getId());
        assertEquals("Electronics", resp.getName());
        assertEquals("img", resp.getCoverImage());
        assertNotNull(resp.getCreatedAt());
    }

    @Test
    void create_throws_whenDuplicateName() {
        CategoryRequest req = new CategoryRequest();
        req.setName("Books");
        when(repository.findByNameIgnoreCase("Books")).thenReturn(Optional.of(new Category()));
        assertThrows(IllegalArgumentException.class, () -> service.create(req));
        verify(repository, never()).save(any());
    }

    @Test
    void update_updatesFields_whenValid() {
        Category existing = new Category();
        existing.setId(7L);
        existing.setName("Old");
        existing.onCreate();
        when(repository.findById(7L)).thenReturn(Optional.of(existing));
        when(repository.findByNameIgnoreCase("New")).thenReturn(Optional.empty());

        CategoryRequest req = new CategoryRequest();
        req.setName("New");
        req.setCoverImage("cimg");

        CategoryResponse resp = service.update(7L, req);
        assertEquals("New", resp.getName());
        assertEquals("cimg", resp.getCoverImage());
    }

    @Test
    void update_throws_whenDuplicateNameOnDifferentId() {
        Category existing = new Category();
        existing.setId(7L);
        existing.setName("Old");
        when(repository.findById(7L)).thenReturn(Optional.of(existing));

        Category other = new Category();
        other.setId(8L);
        other.setName("Conflicting");
        when(repository.findByNameIgnoreCase("Conflicting")).thenReturn(Optional.of(other));

        CategoryRequest req = new CategoryRequest();
        req.setName("Conflicting");

        assertThrows(IllegalArgumentException.class, () -> service.update(7L, req));
    }

    @Test
    void delete_callsRepository_whenExists_elseThrows() {
        when(repository.existsById(3L)).thenReturn(true);
        service.delete(3L);
        verify(repository).deleteById(3L);

        when(repository.existsById(4L)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> service.delete(4L));
    }
}
