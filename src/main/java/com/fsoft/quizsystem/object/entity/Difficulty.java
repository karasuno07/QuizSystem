package com.fsoft.quizsystem.object.entity;

import com.fsoft.quizsystem.object.constant.DifficultyLevel;
import com.fsoft.quizsystem.object.constant.DifficultyPoint;
import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "difficulties")
@Audited
@Getter
@Setter
@NoArgsConstructor
@ToString
@SequenceGenerator(name = "difficulties_id_seq", sequenceName = "difficulties_id_seq", allocationSize = 1)
public class Difficulty {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "difficulties_id_seq")
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

    public Difficulty(DifficultyLevel level, DifficultyPoint point) {
        this.level = level;
        this.point = point;
    }
}