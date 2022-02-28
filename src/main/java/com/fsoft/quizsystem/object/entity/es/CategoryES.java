package com.fsoft.quizsystem.object.entity.es;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;
import java.util.List;

@Document(indexName = "categories")
@Data
public class CategoryES {

    @Id
    private Long id;

    @Field(name = "name", type = FieldType.Text)
    private String name;

    @Field(name = "name", type = FieldType.Keyword)
    private String slug;

    @Field(name = "name", type = FieldType.Auto)
    private String image;

    @Field(type = FieldType.Auto, ignoreFields = {"category"})
    private List<QuizES> quizzes;
}
