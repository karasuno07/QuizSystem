package com.fsoft.quizsystem.repository.spec;

import com.fsoft.quizsystem.object.dto.filter.QuizFilter;
import com.fsoft.quizsystem.object.entity.Quiz;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

public final class QuizSpecification {

    public static Specification<Quiz> getSpecification(QuizFilter filter) {
        return Specification.where(hasTitleContaining(filter.getTitle()))
                            .and(hasStatus(filter.getStatus()))
                            .and(hasCategoryName(filter.getCategoryName()));
    }

    private static Specification<Quiz> hasTitleContaining(String title) {
        return (root, query, builder) ->
                ObjectUtils.isEmpty(title)
                ? builder.conjunction()
                : builder.like(root.get("title"), "%" + title + "%");
    }

    private static Specification<Quiz> hasStatus(String status) {
        return (root, query, builder) ->
                ObjectUtils.isEmpty(status)
                ? builder.conjunction()
                : builder.equal(root.get("status"), status);
    }

    private static Specification<Quiz> hasCategoryName(String categoryName) {
        return (root, query, builder) ->
                ObjectUtils.isEmpty(categoryName)
                ? builder.conjunction()
                : builder.equal(root.get("category").get("name"), categoryName);
    }
}
