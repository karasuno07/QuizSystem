package com.fsoft.quizsystem.object.dto.mapper;

import com.fsoft.quizsystem.object.dto.request.CategoryRequest;
import com.fsoft.quizsystem.object.dto.response.CategoryResponse;
import com.fsoft.quizsystem.object.entity.Category;
import com.fsoft.quizsystem.util.StringUtils;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    Category categoryRequestToEntity(CategoryRequest request);

    void updateEntity(@MappingTarget Category category, CategoryRequest request);

    CategoryResponse entityToCategoryResponse(Category category);

    @AfterMapping
    default void getSlug(@MappingTarget Category category, CategoryRequest request) {
        String slug = StringUtils.generateSlug(request.getName());
        category.setSlug(slug);
    }
}
