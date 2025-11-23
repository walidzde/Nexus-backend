package com.ecommerce_nexus.backend.category;

import com.ecommerce_nexus.backend.category.dto.CategoryRequest;
import com.ecommerce_nexus.backend.category.dto.CategoryResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockBean CategoryService service;
    @MockBean com.ecommerce_nexus.backend.security.JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void getAll_returnsList() throws Exception {
        CategoryResponse c1 = CategoryResponse.builder().id(1L).name("A").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        CategoryResponse c2 = CategoryResponse.builder().id(2L).name("B").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        Mockito.when(service.findAll()).thenReturn(List.of(c1, c2));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("A")));
    }

    @Test
    void getById_returnsItem() throws Exception {
        CategoryResponse c = CategoryResponse.builder().id(5L).name("Shoes").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        Mockito.when(service.findById(5L)).thenReturn(c);

        mockMvc.perform(get("/api/categories/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.name", is("Shoes")));
    }

    @Test
    void create_returns201_andBody() throws Exception {
        CategoryRequest req = new CategoryRequest();
        req.setName("New");
        CategoryResponse created = CategoryResponse.builder().id(10L).name("New").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        Mockito.when(service.create(any(CategoryRequest.class))).thenReturn(created);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.name", is("New")));
    }

    @Test
    void update_returnsOk_andBody() throws Exception {
        CategoryRequest req = new CategoryRequest();
        req.setName("Updated");
        CategoryResponse updated = CategoryResponse.builder().id(3L).name("Updated").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        Mockito.when(service.update(eq(3L), any(CategoryRequest.class))).thenReturn(updated);

        mockMvc.perform(put("/api/categories/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.name", is("Updated")));
    }

    @Test
    void delete_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/categories/9"))
                .andExpect(status().isNoContent());
    }
}
