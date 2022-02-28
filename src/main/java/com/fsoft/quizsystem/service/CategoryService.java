package com.fsoft.quizsystem.service;

import com.fsoft.quizsystem.object.dto.filter.CategoryFilter;
import com.fsoft.quizsystem.object.dto.mapper.CategoryMapper;
import com.fsoft.quizsystem.object.dto.request.CategoryRequest;
import com.fsoft.quizsystem.object.entity.es.CategoryES;
import com.fsoft.quizsystem.object.entity.jpa.Category;
import com.fsoft.quizsystem.object.exception.ResourceNotFoundException;
import com.fsoft.quizsystem.repository.es.CategoryEsRepository;
import com.fsoft.quizsystem.repository.jpa.CategoryRepository;
import com.fsoft.quizsystem.repository.jpa.spec.CategorySpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class CategoryService {

    private final ElasticsearchOperations restTemplate;

    private final CategoryRepository categoryRepository;
    private final CategoryEsRepository categoryEsRepository;
    private final CategoryMapper categoryMapper;

    private final CloudinaryService cloudinaryService;

    public Page<Category> findAllCategories(CategoryFilter filter) {
        Page<Category> categories;

        BoolQueryBuilder query = QueryBuilders.boolQuery();
        if (!ObjectUtils.isEmpty(filter.getName())) {
            query.should(QueryBuilders.matchQuery("name", filter.getName()));
        }

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(query)
                .withPageable(filter.getPagination().getPageAndSort())
                .build();
        SearchHits<CategoryES> hits = restTemplate.search(searchQuery, CategoryES.class,
                                                          IndexCoordinates.of("categories"));
        if (hits.hasSearchHits()) {
            SearchPage<CategoryES> page = SearchHitSupport.searchPageFor(hits, filter.getPagination()
                                                                                     .getPageAndSort());
            categories = page.map(SearchHit::getContent)
                             .map(categoryMapper::esEntityToJpa);
        } else {
            Specification<Category> specification = CategorySpecification.getSpecification(filter);
            categories = categoryRepository.findAll(specification, filter.getPagination().getPageAndSort());
        }

        return categories;
    }

    public Category findCategoryById(long id) {
        Optional<CategoryES> optional = categoryEsRepository.findById(id);
        if (optional.isPresent()) {
            return categoryMapper.esEntityToJpa(optional.get());
        } else {
            return categoryRepository.findById(id).orElseThrow(
                    () -> new ResourceNotFoundException("Not found any category with id " + id));
        }
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
