package com.fsoft.quizsystem.object.model;

import com.fsoft.quizsystem.object.entity.User;
import lombok.Data;

@Data
public class RefreshToken {
    private String token;
    private User user;
}
