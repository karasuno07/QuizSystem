package com.fsoft.quizsystem.object.model;

import com.fsoft.quizsystem.object.entity.jpa.User;
import lombok.Data;

@Data
public class RefreshToken {
    private String token;
    private User user;
}
