package com.fsoft.quizsystem.object.dto.response;

import lombok.Data;

@Data
public class AnswerResponse {

    private Long id;
    private String text;
    private Boolean isCorrect;
}
