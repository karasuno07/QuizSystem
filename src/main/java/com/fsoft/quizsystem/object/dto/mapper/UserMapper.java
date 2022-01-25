package com.fsoft.quizsystem.object.dto.mapper;

import com.fsoft.quizsystem.object.dto.request.UserRequest;
import com.fsoft.quizsystem.object.dto.response.AuthenticationInfo;
import com.fsoft.quizsystem.object.dto.response.UserResponse;
import com.fsoft.quizsystem.object.entity.User;
import org.mapstruct.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.ObjectUtils;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    AuthenticationInfo entityToAuthInfo(User user);

    @AfterMapping
    default void mappingEntityPropsToAuthInfo(@MappingTarget AuthenticationInfo authInfo, User user) {
        if (ObjectUtils.isEmpty(user.getRole())) return;

        authInfo.setRoleName(user.getRole().getName());

        if (!ObjectUtils.isEmpty(user.getRole().getAuthorities())) {
            Set<String> permissions = user.getAuthorities().stream()
                                          .map(GrantedAuthority::getAuthority)
                                          .collect(Collectors.toSet());
            authInfo.setPermissions(permissions);
        }
    }

    @Mapping(target = "roleName", source = "role.name")
    UserResponse entityToUserResponse(User user);

    @Mapping(target = "fullName", ignore = true)
    User userRequestToEntity(UserRequest request);

    @Mapping(target = "fullName", ignore = true)
    void updateEntity(@MappingTarget User user, UserRequest request);

    @AfterMapping
    default void mappingReqPropsToEntity(@MappingTarget User user, UserRequest request) {
        String fullName = request.getFullName().getFirstName() + " " + request.getFullName().getLastName();
        user.setFullName(fullName);
    }

}
