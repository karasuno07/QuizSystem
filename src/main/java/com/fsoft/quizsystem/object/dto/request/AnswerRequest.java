package com.fsoft.quizsystem.object.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AnswerRequest {

    @NotBlank(message = "blank")
    private String text;

    private Boolean isCorrect;
}
