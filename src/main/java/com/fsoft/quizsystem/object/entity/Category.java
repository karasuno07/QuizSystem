package com.fsoft.quizsystem.object.entity;

import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "categories")
@Audited
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE) @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String image;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL) @ToString.Exclude
    private Set<Quiz> quizzes;
}