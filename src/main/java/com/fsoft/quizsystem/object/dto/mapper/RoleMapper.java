package com.fsoft.quizsystem.object.dto.mapper;

import com.fsoft.quizsystem.object.dto.response.RoleResponse;
import com.fsoft.quizsystem.object.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {
    RoleResponse entityToRoleResponse(Role role);
}
