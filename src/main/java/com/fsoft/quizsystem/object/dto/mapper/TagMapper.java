package com.fsoft.quizsystem.object.dto.mapper;

import com.fsoft.quizsystem.object.dto.request.TagRequest;
import com.fsoft.quizsystem.object.dto.response.TagResponse;
import com.fsoft.quizsystem.object.entity.es.TagES;
import com.fsoft.quizsystem.object.entity.jpa.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {

    Tag tagReqeustToEntity(TagRequest request);

    void updateEntity(@MappingTarget Tag tag, TagRequest request);

    Tag esEntityToJpa(TagES entity);

    TagResponse entityToTagResponse(Tag tag);
}
