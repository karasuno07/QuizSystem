package com.fsoft.quizsystem.object.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserResponse {

    private Long id;

    private String username;

    private String fullName;

    private String email;

    private String phoneNumber;

    private String image;

    @JsonProperty("role")
    private String roleName;
}
