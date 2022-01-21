package com.fsoft.quizsystem.object.entity;

import com.fsoft.quizsystem.object.constant.QuizStatus;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "quizzes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(columnDefinition = "varchar(255)", nullable = false)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Column(columnDefinition = "text")
    private String image;

    @Enumerated(EnumType.STRING)
    private QuizStatus status;

    @ManyToOne @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL) @ToString.Exclude
    private List<Question> questions;

    @ManyToOne @JoinColumn(name = "instructor_id")
    private User instructor;
}