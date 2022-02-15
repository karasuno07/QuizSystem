package com.fsoft.quizsystem.controller;

import com.fsoft.quizsystem.object.dto.filter.CategoryFilter;
import com.fsoft.quizsystem.object.dto.mapper.CategoryMapper;
import com.fsoft.quizsystem.object.dto.request.CategoryRequest;
import com.fsoft.quizsystem.object.dto.response.CategoryResponse;
import com.fsoft.quizsystem.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController implements SecuredBearerTokenController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @PostMapping("/all")
    ResponseEntity<?> getAllCategories(@RequestBody Optional<CategoryFilter> filter) {
        Page<CategoryResponse> responses = categoryService.findAllCategories(filter.orElse(new CategoryFilter()))
                                                          .map(categoryMapper::entityToCategoryResponse);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        CategoryResponse response =
                categoryMapper.entityToCategoryResponse(categoryService.findCategoryById(id));
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    ResponseEntity<?> createCategory(@ModelAttribute @Valid CategoryRequest requestBody) {
        CategoryResponse response =
                categoryMapper.entityToCategoryResponse(categoryService.createCategory(requestBody));
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    ResponseEntity<?> updateCategory(@PathVariable Long id, @ModelAttribute @Valid CategoryRequest requestBody) {
        CategoryResponse response =
                categoryMapper.entityToCategoryResponse(categoryService.updateCategory(id, requestBody));
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }
}
