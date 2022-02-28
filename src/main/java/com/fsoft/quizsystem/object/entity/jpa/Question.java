package com.fsoft.quizsystem.object.entity.jpa;

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
@SequenceGenerator(name = "questions_id_seq", sequenceName = "questions_id_seq", allocationSize = 1)
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "questions_id_seq")
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

    @ManyToOne @JoinColumn(name = "quiz_id")
    private Quiz quiz;
}
