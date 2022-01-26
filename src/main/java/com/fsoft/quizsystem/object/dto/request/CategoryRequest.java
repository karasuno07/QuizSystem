package com.fsoft.quizsystem.object.dto.request;

import com.fsoft.quizsystem.object.validation.NotSupportedImageType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CategoryRequest {

    @NotBlank(message = "blank")
    @Size(min = 3, max = 50, message = "size(min:3,max:50)")
    private String name;

    @NotSupportedImageType
    private MultipartFile imageFile;
}
