package com.fsoft.quizsystem.object.entity;

import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "users")
@Audited
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User implements UserDetails, OAuth2User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(columnDefinition = "varchar(50)", nullable = false, unique = true)
    private String username;

    @Column(columnDefinition = "varchar(80)", nullable = false)
    private String password;

    @Column(columnDefinition = "varchar(50)", nullable = false)
    private String fullName;

    @Column(columnDefinition = "varchar(100)", unique = true)
    private String email;

    @Column(columnDefinition = "varchar(12)")
    private String phoneNumber;

    @Column(columnDefinition = "text")
    private String image;

    @ManyToOne @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "instructor") @ToString.Exclude
    private Set<Quiz> quizSet;

    private Boolean status = true;

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(
                ObjectUtils.isEmpty(role)
                ? new SimpleGrantedAuthority("ROLE_USER")
                : new SimpleGrantedAuthority("ROLE_" + role.getName().name()));
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

    @Override
    public String getName() {
        return null;
    }
}