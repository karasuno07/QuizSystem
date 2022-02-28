package com.fsoft.quizsystem.object.dto.mapper;

import com.fsoft.quizsystem.object.dto.request.AnswerRequest;
import com.fsoft.quizsystem.object.dto.response.AnswerResponse;
import com.fsoft.quizsystem.object.entity.jpa.Answer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnswerMapper {

    Answer answerRequestToEntity(AnswerRequest request);

    void updateEntity(@MappingTarget Answer answer, AnswerRequest request);

    AnswerResponse entityToAnswerResponse(Answer answer);
}
