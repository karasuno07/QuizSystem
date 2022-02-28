package com.fsoft.quizsystem.object.dto.mapper;

import com.fsoft.quizsystem.object.constant.DifficultyPoint;
import com.fsoft.quizsystem.object.dto.response.DifficultyResponse;
import com.fsoft.quizsystem.object.entity.es.DifficultyES;
import com.fsoft.quizsystem.object.entity.jpa.Difficulty;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DifficultyMapper {

    DifficultyResponse entityToDifficultyResponse(Difficulty difficulty);

    Difficulty esEntityToJpa(DifficultyES entity);

    default Integer mapDifficultyPointToInteger(DifficultyPoint value) {
        return value.getPoint();
    }
}