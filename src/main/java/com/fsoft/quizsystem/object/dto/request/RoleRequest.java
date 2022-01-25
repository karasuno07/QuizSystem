package com.fsoft.quizsystem.object.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class RoleRequest {

    @NotBlank(message = "blank")
    @Size(min = 2, max = 30, message = "size(min:2,max:30)")
    private String name;

    @NotEmpty(message = "empty")
    private Set<String> authorities;
}
