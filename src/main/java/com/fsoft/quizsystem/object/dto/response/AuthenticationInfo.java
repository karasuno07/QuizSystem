package com.fsoft.quizsystem.object.dto.response;

import lombok.Data;

import java.util.Set;

@Data
public class AuthenticationInfo {
    private long id;
    private String username;
    private String roleName;
    private Set<String> permissions;
}
