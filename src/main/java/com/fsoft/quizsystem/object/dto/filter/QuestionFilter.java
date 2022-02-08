package com.fsoft.quizsystem.object.dto.filter;

import com.fsoft.quizsystem.object.model.Pagination;
import lombok.Data;

@Data
public class QuestionFilter {

    private Long tagId;
    private Long difficultyId;
    private Pagination pagination = new Pagination(10);
}
