package com.fsoft.quizsystem.object.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(columnDefinition = "varchar(50)", nullable = false)
    private String username;

    @Column(columnDefinition = "varchar(80)", nullable = false)
    private String password;

    @Column(columnDefinition = "varchar(50)", nullable = false)
    private String firstName;

    @Column(columnDefinition = "varchar(50)", nullable = false)
    private String lastName;

    @Column(columnDefinition = "varchar(100)")
    private String email;

    @Column(columnDefinition = "varchar(12)")
    private String phoneNumber;

    @Column(columnDefinition = "text")
    private String image;

    @ManyToOne @JoinColumn(name = "role_id")
    private Role role;

    private Boolean status;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(role.getAuthorities().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return status;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return status;
    }

    @Override
    public boolean isEnabled() {
        return status;
    }
}