package com.fsoft.quizsystem.object.dto.filter;

import com.fsoft.quizsystem.object.model.Pagination;
import lombok.Data;

@Data
public class QuizFilter {

    private String title;

    private String status;

    private String categoryName;

    private Pagination pagination = new Pagination(10);
}
