package com.fsoft.quizsystem.object.entity;

import com.fsoft.quizsystem.object.constant.SystemAuthorities;
import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "roles")
@Audited
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(columnDefinition = "varchar(50)", nullable = false)
    private String name;

    @Column(columnDefinition = "text")
    @Enumerated
    @ElementCollection(targetClass = SystemAuthorities.class)
    private Set<SystemAuthorities> authorities;
}