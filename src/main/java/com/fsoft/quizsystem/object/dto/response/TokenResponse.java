package com.fsoft.quizsystem.object.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonPropertyOrder({"accessToken", "refreshToken", "tokenType", "user"})
public class TokenResponse {

    private String accessToken;

    private String refreshToken;

    private String tokenType = "Bearer";

    private UserResponse user;

    public TokenResponse(String accessToken,
                         String refreshToken,
                         UserResponse user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.user = user;
    }
}