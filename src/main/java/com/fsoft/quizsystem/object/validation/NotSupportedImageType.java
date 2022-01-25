package com.fsoft.quizsystem.object.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = ImageTypeValidator.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface NotSupportedImageType {

    String message() default "Invalid image type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
