package com.fsoft.quizsystem.repository.jpa.spec;

import com.fsoft.quizsystem.object.dto.filter.UserFilter;
import com.fsoft.quizsystem.object.entity.jpa.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

public final class UserSpecification {

    public static Specification<User> getSpecification(UserFilter filter) {
        return Specification.where(hasUsername(filter.getUsername()))
                            .and(hasFullNameContaining(filter.getFullName()))
                            .and(hasEmail(filter.getEmail()))
                            .and(hasPhoneNumber(filter.getPhoneNumber()));
    }

    private static Specification<User> hasUsername(String username) {
        return (root, query, builder) ->
                ObjectUtils.isEmpty(username)
                ? builder.conjunction()
                : builder.equal(root.get("username"), username);
    }


    private static Specification<User> hasFullNameContaining(String fullName) {
        return (root, query, builder) ->
                ObjectUtils.isEmpty(fullName)
                ? builder.conjunction()
                : builder.like(root.get("fullName"), "%" + fullName + "%");
    }

    private static Specification<User> hasEmail(String email) {
        return (root, query, builder) ->
                ObjectUtils.isEmpty(email)
                ? builder.conjunction()
                : builder.equal(root.get("email"), email);
    }

    private static Specification<User> hasPhoneNumber(String phoneNumber) {
        return (root, query, builder) ->
                ObjectUtils.isEmpty(phoneNumber)
                ? builder.conjunction()
                : builder.equal(root.get("phoneNumber"), phoneNumber);
    }
}
