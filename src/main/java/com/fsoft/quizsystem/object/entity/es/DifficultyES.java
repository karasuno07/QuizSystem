package com.fsoft.quizsystem.object.entity.es;

import com.fsoft.quizsystem.object.constant.DifficultyLevel;
import com.fsoft.quizsystem.object.constant.DifficultyPoint;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.util.List;

@Document(indexName = "difficulties")
@Data
public class DifficultyES {

    @Id
    private Long id;

    @Field(name = "level", type = FieldType.Keyword)
    @Enumerated(EnumType.STRING)
    private DifficultyLevel level;

    @Field(name = "point", type = FieldType.Integer)
    @Enumerated(EnumType.ORDINAL)
    private DifficultyPoint point;

    @Field(name = "questions", type = FieldType.Nested, ignoreFields = {"difficulty"})
    private List<QuestionES> questions;
}
