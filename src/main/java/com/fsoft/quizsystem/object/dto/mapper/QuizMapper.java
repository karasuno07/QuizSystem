package com.fsoft.quizsystem.object.dto.mapper;

import com.fsoft.quizsystem.object.dto.request.QuizRequest;
import com.fsoft.quizsystem.object.dto.response.QuizResponse;
import com.fsoft.quizsystem.object.entity.Quiz;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface QuizMapper {

    Quiz quizRequestToEntity(QuizRequest request);

    void updateEntity(@MappingTarget Quiz quiz, QuizRequest request);

    @Mapping(target = "instructorName", source = "instructor.name")
    @Mapping(target = "categoryName", source = "category.name")
    QuizResponse entityToQuizResponse(Quiz quiz);
}
