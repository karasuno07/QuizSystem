package com.fsoft.quizsystem.object.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class FullName {

    @NotBlank(message = "blank")
    @Size(min = 2, max = 30, message = "size(min:2,max:30)")
    private String firstName;

    @NotBlank(message = "blank")
    @Size(min = 2, max = 30, message = "size(min:2,max:30)")
    private String lastName;
}
