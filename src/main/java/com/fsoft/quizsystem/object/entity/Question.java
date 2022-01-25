package com.fsoft.quizsystem.object.entity;

import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.*;
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

    private String title;

    @OneToMany(mappedBy = "question") @ToString.Exclude
    private Set<Answer> answers;

    private Boolean isMultiple;

    @ManyToOne @JoinColumn(name = "tag_id")
    private Tag tag;

    @ManyToOne @JoinColumn(name = "difficulty_id")
    private Difficulty difficulty;

    @ManyToOne @JoinColumn(name = "quiz_id") private Quiz quiz;
}
