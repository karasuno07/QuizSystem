package com.fsoft.quizsystem.object.dto.request;

import com.fsoft.quizsystem.object.model.FullName;
import com.fsoft.quizsystem.object.validation.NotSupportedImageType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserRequest {

    @NotBlank(message = "blank")
    private String username;

    @NotBlank(message = "blank")
    private String password;

    @NotNull(message = "null")
    private FullName fullName;

    @NotBlank(message = "blank")
    @Email(message = "invalid email format")
    private String email;

    @NotBlank(message = "blank")
    private String phoneNumber;

    @NotSupportedImageType
    private MultipartFile imageFile;

    private Long roleId;
}
