package com.fsoft.quizsystem.object.dto.mapper;

import com.fsoft.quizsystem.object.dto.request.QuestionRequest;
import com.fsoft.quizsystem.object.dto.response.QuestionResponse;
import com.fsoft.quizsystem.object.entity.Question;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = DifficultyMapper.class)
public interface QuestionMapper {

    Question questionRequestToEntity(QuestionRequest request);

    void updateEntity(@MappingTarget Question question, QuestionRequest request);

    QuestionResponse entityToQuestionResponse(Question question);
}
