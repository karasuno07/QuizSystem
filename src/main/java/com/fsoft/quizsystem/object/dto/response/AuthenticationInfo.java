package com.fsoft.quizsystem.object.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Set;

@Data
public class AuthenticationInfo {

    private long id;

    private String username;

    @JsonProperty("role")
    private String roleName;
}
