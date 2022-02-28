package com.fsoft.quizsystem.object.dto.mapper;

import com.fsoft.quizsystem.object.dto.request.UserRequest;
import com.fsoft.quizsystem.object.dto.response.AuthenticationInfo;
import com.fsoft.quizsystem.object.dto.response.UserResponse;
import com.fsoft.quizsystem.object.entity.es.UserES;
import com.fsoft.quizsystem.object.entity.jpa.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "roleName", source = "role.name")
    AuthenticationInfo entityToAuthInfo(User user);

    @Mapping(target = "roleName", source = "role.name")
    UserResponse entityToUserResponse(User user);

    @Mapping(target = "fullName", ignore = true)
    User userRequestToEntity(UserRequest request);

    @Mapping(target = "fullName", ignore = true)
    void updateEntity(@MappingTarget User user, UserRequest request);

    User esEntityToJpa(UserES entity);

    @AfterMapping
    default void mappingReqPropsToEntity(@MappingTarget User user, UserRequest request) {
        String fullName =
                request.getFullName().getFirstName() + " " + request.getFullName().getLastName();
        user.setFullName(fullName);
    }

}
