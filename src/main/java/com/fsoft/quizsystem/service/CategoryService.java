package com.fsoft.quizsystem.service;

import com.fsoft.quizsystem.object.dto.mapper.CategoryMapper;
import com.fsoft.quizsystem.object.dto.request.CategoryRequest;
import com.fsoft.quizsystem.object.entity.Category;
import com.fsoft.quizsystem.object.exception.ResourceNotFoundException;
import com.fsoft.quizsystem.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @PostConstruct
    private void init() {
        if (categoryRepository.count() == 0) {
            List<Category> initialCategories = Arrays.asList(
                    new Category("Development", "development", "https://res.cloudinary.com/fpt-software-quiz/image/upload/v1644660774/developement_zah93x.jpg"),
                    new Category("Business", "business", "https://res.cloudinary.com/fpt-software-quiz/image/upload/v1644660774/business_niyc4b.jpg"),
                    new Category("Design", "design", "https://res.cloudinary.com/fpt-software-quiz/image/upload/v1644660774/design_k3sin6.jpg"),
                    new Category("IT and Software", "it-and-software", "https://res.cloudinary.com/fpt-software-quiz/image/upload/v1644660774/it-and-software_zguif9.jpg"),
                    new Category("Marketing", "marketing", "https://res.cloudinary.com/fpt-software-quiz/image/upload/v1644660774/marketing_bliev0.jpg"),
                    new Category("Music", "music", "https://res.cloudinary.com/fpt-software-quiz/image/upload/v1644660774/music_t4mncd.jpg"),
                    new Category("Personal Development", "personal-development", "https://res.cloudinary.com/fpt-software-quiz/image/upload/v1644660774/personal-developement_npjujq.jpg"),
                    new Category("Photography", "photography", "https://res.cloudinary.com/fpt-software-quiz/image/upload/v1644660774/photography_kdazmf.jpg")
            );
            categoryRepository.saveAll(initialCategories);
        }
    }

    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    public Page<Category> findAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    public Category findCategoryById(long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Not found any category with id " + id));
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
