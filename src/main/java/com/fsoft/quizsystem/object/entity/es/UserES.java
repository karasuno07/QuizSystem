package com.fsoft.quizsystem.object.entity.es;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;
import java.util.List;

@Document(indexName = "users")
@Data
public class UserES {

    @Id
    private Long id;

    @Field(name = "username", type = FieldType.Keyword)
    private String username;

    @Field(name = "password", type = FieldType.Text)
    private String password;

    @Field(name = "full_name", type = FieldType.Text)
    private String fullName;

    @Field(name = "email", type = FieldType.Keyword, normalizer = "lowercase")
    private String email;

    @Field(name = "phone_number", type = FieldType.Text)
    private String phoneNumber;

    @Field(name = "image", type = FieldType.Auto)
    private String image;

    @Field(name = "role", type = FieldType.Nested)
    private RoleES role;

    @Field(name = "quizzes", type = FieldType.Nested, ignoreFields = {"instructor"})
    private List<QuizES> quizzes;

    @Field(name = "favorite_categories", type = FieldType.Nested)
    private List<CategoryES> favoriteCategories;

    @Field(name = "accept_email", type = FieldType.Boolean)
    private Boolean notification;

    @Field(name = "active", type = FieldType.Boolean)
    private Boolean active;
}
