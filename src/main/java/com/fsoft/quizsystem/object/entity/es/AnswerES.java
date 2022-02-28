package com.fsoft.quizsystem.object.entity.es;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;

@Document(indexName = "answers")
@Data
public class AnswerES {

    @Id
    private Long id;

    @Field(name = "text", type = FieldType.Text)
    private String text;

    @Field(name = "is_correct", type = FieldType.Boolean)
    private Boolean isCorrect;

    @Field(type = FieldType.Nested, ignoreFields = {"answers"})
    private QuestionES question;
}
