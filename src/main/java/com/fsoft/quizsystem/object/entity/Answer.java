package com.fsoft.quizsystem.object.entity;

import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@Table(name = "answers")
@Audited
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SequenceGenerator(name = "answers_id_seq", sequenceName = "answers_id_seq", allocationSize = 1)
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "answers_id_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(columnDefinition = "text", nullable = false)
    private String text;

    private Boolean isCorrect;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
}