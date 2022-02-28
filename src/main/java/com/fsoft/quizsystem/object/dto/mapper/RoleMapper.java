package com.fsoft.quizsystem.object.dto.mapper;

import com.fsoft.quizsystem.object.dto.response.RoleResponse;
import com.fsoft.quizsystem.object.entity.es.RoleES;
import com.fsoft.quizsystem.object.entity.jpa.Role;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {
    RoleResponse entityToRoleResponse(Role role);

    Role esEntityToJpa(RoleES entity);
}
