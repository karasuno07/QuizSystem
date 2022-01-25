package com.fsoft.quizsystem.object.validation;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class ImageTypeValidator implements ConstraintValidator<NotSupportedImageType, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) return true;
        return isSupportedContentType(file.getContentType());
    }

    private boolean isSupportedContentType(String contentType) {
        List<String> supportedContents = Arrays.asList("image/jpg", "image/jpeg", "image/png");
        return supportedContents.contains(contentType);
    }
}
