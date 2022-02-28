package com.fsoft.quizsystem.object.entity.es;

import com.fsoft.quizsystem.object.constant.QuizStatus;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.util.List;

@Document(indexName = "quizzes")
@Data
public class QuizES {

    @Id
    private Long id;

    @Field(name = "title", type = FieldType.Text)
    private String title;

    @Field(name = "description", type = FieldType.Text)
    private String description;

    @Field(name = "image", type = FieldType.Auto)
    private String image;

    @Field(name = "status", type = FieldType.Keyword)
    @Enumerated(EnumType.STRING)
    private QuizStatus status;

    @Field(name = "category", type = FieldType.Nested, ignoreFields = {"quizzes"})
    private CategoryES category;

    @Field(name = "questions", type = FieldType.Nested, ignoreFields = {"quiz"})
    private List<QuestionES> questions;

    @Field(name = "instructor", type = FieldType.Nested, ignoreFields = {"quizzes"})
    private UserES instructor;
}
