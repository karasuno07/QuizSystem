package com.fsoft.quizsystem.repository.spec;

import com.fsoft.quizsystem.object.dto.filter.QuestionFilter;
import com.fsoft.quizsystem.object.entity.Question;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

public final class QuestionSpecification {

    public static Specification<Question> getSpecification(Long quizId, QuestionFilter filter) {
        return Specification.where(hasQuizId(quizId))
                            .and(hasTagId(filter.getTagId()))
                            .and(hasDifficultyId(filter.getDifficultyId()));
    }

    private static Specification<Question> hasQuizId(Long quizId) {
        return (root, query, builder) ->
                ObjectUtils.isEmpty(quizId)
                ? builder.conjunction()
                : builder.equal(root.get("quiz").get("id"), quizId);
    }

    private static Specification<Question> hasTagId(Long tagId) {
        return (root, query, builder) ->
                ObjectUtils.isEmpty(tagId)
                ? builder.conjunction()
                : builder.equal(root.get("tag").get("id"), tagId);
    }

    private static Specification<Question> hasDifficultyId(Long difficultyId) {
        return (root, query, builder) ->
                ObjectUtils.isEmpty(difficultyId)
                ? builder.conjunction()
                : builder.equal(root.get("difficulty").get("id"), difficultyId);
    }
}
