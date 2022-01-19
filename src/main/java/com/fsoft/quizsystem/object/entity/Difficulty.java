package com.fsoft.quizsystem.object.entity;

import com.fsoft.quizsystem.object.constant.DifficultyLevel;
import com.fsoft.quizsystem.object.constant.DifficultyPoint;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "difficulties")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Difficulty {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DifficultyLevel level;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private DifficultyPoint point;

    @OneToMany(mappedBy = "difficulty") @ToString.Exclude
    private Set<Question> questions;
}