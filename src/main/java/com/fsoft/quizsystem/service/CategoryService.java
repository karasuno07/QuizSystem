package com.fsoft.quizsystem.service;

import com.fsoft.quizsystem.object.dto.filter.CategoryFilter;
import com.fsoft.quizsystem.object.dto.mapper.CategoryMapper;
import com.fsoft.quizsystem.object.dto.request.CategoryRequest;
import com.fsoft.quizsystem.object.entity.Category;
import com.fsoft.quizsystem.object.exception.ResourceNotFoundException;
import com.fsoft.quizsystem.repository.CategoryRepository;
import com.fsoft.quizsystem.repository.spec.CategorySpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    private final CloudinaryService cloudinaryService;

    public Page<Category> findAllCategories(CategoryFilter filter) {
        Specification<Category> specification = CategorySpecification.getSpecification(filter);
        return categoryRepository.findAll(specification, filter.getPagination().getPageAndSort());
    }

    public Category findCategoryById(long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Not found any category with id " + id));
    }

    public Category findCategoryByName(String name) {
        return categoryRepository.findByName(name).orElseThrow(
                () -> new ResourceNotFoundException("Not found any category with name " + name));
    }

    public Category createCategory(CategoryRequest requestBody) {
        Category category = categoryMapper.categoryRequestToEntity(requestBody);

        if (!ObjectUtils.isEmpty(requestBody.getImageFile())) {
            String image = cloudinaryService.uploadImage(null, requestBody.getImageFile());
            if (image != null) category.setImage(image);
        }

        return categoryRepository.save(category);
    }

    public Category updateCategory(long id, CategoryRequest requestBody) {
        Category category = this.findCategoryById(id);
        categoryMapper.updateEntity(category, requestBody);

        if (!ObjectUtils.isEmpty(requestBody.getImageFile())) {
            String image = cloudinaryService.uploadImage(category.getImage(), requestBody.getImageFile());
            if (image != null) category.setImage(image);
        }

        return categoryRepository.save(category);
    }

    public void deleteCategory(long id) {
        Category category = this.findCategoryById(id);

        categoryRepository.delete(category);
    }

}
