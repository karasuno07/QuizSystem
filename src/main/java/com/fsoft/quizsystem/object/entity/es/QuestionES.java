package com.fsoft.quizsystem.object.entity.es;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;
import java.util.List;

@Document(indexName = "questions")
@Data
public class QuestionES {

    @Id
    private Long id;

    @Field(name = "title", type = FieldType.Text)
    private String title;

    @Field(name = "answers", type = FieldType.Auto, ignoreFields = {"question"})
    private List<AnswerES> answers;

    @Field(name = "is_multiple", type = FieldType.Boolean)
    private Boolean isMultiple;

    @Field(name = "tag", type = FieldType.Nested, ignoreFields = {"questions"})
    private TagES tag;

    @Field(name = "difficulty", type = FieldType.Nested, ignoreFields = {"questions"})
    private DifficultyES difficulty;

    @Field(name = "quiz", type = FieldType.Nested, ignoreFields = {"question"})
    private QuizES quiz;
}
