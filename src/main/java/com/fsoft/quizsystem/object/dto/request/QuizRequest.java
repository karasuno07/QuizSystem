package com.fsoft.quizsystem.object.dto.request;

import com.fsoft.quizsystem.object.validation.NotSupportedImageType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class QuizRequest {

    @NotBlank(message = "blank")
    @Size(min = 10, max = 255, message = "size(min:10,max:255)")
    private String title;

    @NotBlank(message = "blank")
    private String description;

    @NotSupportedImageType
    private MultipartFile imageFile;

    @NotNull(message = "null")
    private Long categoryId;
}
