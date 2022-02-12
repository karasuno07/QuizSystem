package com.fsoft.quizsystem.object.entity;

import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "questions")
@Audited
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(columnDefinition = "varchar(255)", nullable = false)
    private String title;

    @OneToMany(mappedBy = "question") @ToString.Exclude
    private Set<Answer> answers;

    private Boolean isMultiple = false;

    @ManyToOne @JoinColumn(name = "tag_id")
    private Tag tag;

    @ManyToOne @JoinColumn(name = "difficulty_id")
    private Difficulty difficulty;

    @ManyToOne @JoinColumn(name = "quiz_id") private Quiz quiz;

    public void addAnswer(Answer answer) {
        if (ObjectUtils.isEmpty(answers)) answers = new HashSet<>();
        answers.add(answer);
        answer.setQuestion(this);
    }
}
