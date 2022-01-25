package com.fsoft.quizsystem.object.dto.response;

import lombok.Data;

import java.util.Set;

@Data
public class RoleResponse {

    private Long id;
    private String name;
    private Set<String> authorities;
}
