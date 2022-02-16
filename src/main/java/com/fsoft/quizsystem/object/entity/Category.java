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
@SequenceGenerator(name = "categories_id_seq", sequenceName = "categories_id_seq", allocationSize = 1)
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categories_id_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(columnDefinition = "varchar(50)", nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "varchar(50)", nullable = false)
    private String slug;

    @Column(columnDefinition = "text")
    private String image;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL) @ToString.Exclude
    private Set<Quiz> quizzes;
}