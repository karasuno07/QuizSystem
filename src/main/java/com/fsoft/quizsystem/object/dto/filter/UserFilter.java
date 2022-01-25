package com.fsoft.quizsystem.object.dto.filter;

import com.fsoft.quizsystem.object.model.Pagination;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class UserFilter {

    private String username;

    private String fullName;

    private String email;

    private String phoneNumber;

    private Boolean active;

    private Pagination pagination = new Pagination(10);
}
