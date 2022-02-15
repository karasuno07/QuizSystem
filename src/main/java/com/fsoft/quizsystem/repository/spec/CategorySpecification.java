package com.fsoft.quizsystem.repository.spec;

import com.fsoft.quizsystem.object.dto.filter.CategoryFilter;
import com.fsoft.quizsystem.object.entity.Category;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

public final class CategorySpecification {

    public static Specification<Category> getSpecification(CategoryFilter filter) {
        return Specification.where(hasCategoryName(filter.getName()));
    }

    private static Specification<Category> hasCategoryName(String name) {
        return (root, query, builder) ->
                ObjectUtils.isEmpty(name)
                        ? builder.conjunction()
                        : builder.like(root.get("name"), "%" + name + "%");
    }

}
