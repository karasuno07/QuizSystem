package com.fsoft.quizsystem.object.entity.es;

import com.fsoft.quizsystem.object.constant.SystemRole;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Document(indexName = "roles")
@Data
public class RoleES {

    @Id
    private Long id;

    @Field(name = "name", type = FieldType.Keyword)
    @Enumerated(EnumType.STRING)
    private SystemRole name;
}
