package com.fsoft.quizsystem.object.dto.response;

import lombok.Data;

@Data
public class QuizResponse {

    private Long id;
    private String title;
    private String description;
    private String image;
    private String status;
    private String categoryName;
    private String instructorName;
}
