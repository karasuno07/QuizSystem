package com.fsoft.quizsystem.object.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class QuestionRequest {

    @NotBlank(message = "blank")
    @Size(min = 10, max = 255, message = "size(min:10,max:255)")
    private String title;

    @NotNull(message = "null")
    private Long tagId;

    @NotNull(message = "null")
    private Long difficultyId;

    private Boolean isMultiple;

    @NotNull(message = "null")
    private Long quizId;

    @NotEmpty(message = "empty")
    private List<AnswerRequest> answers;
}
