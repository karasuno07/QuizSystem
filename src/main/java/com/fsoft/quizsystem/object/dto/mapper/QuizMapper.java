package com.fsoft.quizsystem.object.dto.mapper;

import com.fsoft.quizsystem.object.dto.request.QuizRequest;
import com.fsoft.quizsystem.object.dto.response.QuizResponse;
import com.fsoft.quizsystem.object.entity.es.QuizES;
import com.fsoft.quizsystem.object.entity.jpa.Quiz;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface QuizMapper {

    Quiz quizRequestToEntity(QuizRequest request);

    void updateEntity(@MappingTarget Quiz quiz, QuizRequest request);

    Quiz esEntityToJpa(QuizES entity);

    @Mapping(target = "instructorName", source = "instructor.fullName")
    @Mapping(target = "categoryName", source = "category.name")
    QuizResponse entityToQuizResponse(Quiz quiz);
}
