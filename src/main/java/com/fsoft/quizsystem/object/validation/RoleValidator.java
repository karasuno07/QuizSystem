package com.fsoft.quizsystem.object.validation;

import com.fsoft.quizsystem.object.entity.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public final class RoleValidator {

    public static boolean isAdmin(User user) {
        return user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }
}
