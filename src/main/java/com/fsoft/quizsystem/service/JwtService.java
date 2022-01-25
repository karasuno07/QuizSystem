package com.fsoft.quizsystem.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.impl.JWTParser;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.Payload;
import com.fsoft.quizsystem.object.dto.mapper.UserMapper;
import com.fsoft.quizsystem.object.dto.response.AuthenticationInfo;
import com.fsoft.quizsystem.object.entity.User;
import com.fsoft.quizsystem.object.model.RefreshToken;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Log4j2
public class JwtService {

    @Value("${token.jwt.secret-key}")
    private String secretKey;

    @Value("${token.jwt.expiration-time}")
    private Long expirationTime;

    private final RedissonClient redissonClient;
    private final UserMapper userMapper;

    private RMapCache<String, Long> userIdMap;
    private RMapCache<Long, User> tokenMap;
    private final TimeUnit timeUnit = TimeUnit.HOURS;

    @PostConstruct
    public void init() {
        userIdMap = redissonClient.getMapCache("USER_ID_MAP");
        tokenMap = redissonClient.getMapCache("REFRESH_TOKEN_MAP");
    }

    public String generateAccessToken(@NonNull User user) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", user.getId());
        payload.put("username", user.getUsername());
        if (!ObjectUtils.isEmpty(user.getRole())) {
            payload.put("role", user.getRole().getName());
            if (!ObjectUtils.isEmpty(user.getRole().getAuthorities())) {
                payload.put("permissions", user.getRole().getAuthorities());
            }
        }

        JWTCreator.Builder builder = JWT.create();
        builder.withKeyId(UUID.randomUUID().toString())
               .withSubject(user.getId().toString())
               .withPayload(payload)
               .withIssuedAt(new Date())
               .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime));

        return builder.sign(Algorithm.HMAC512(secretKey));
    }

    public AuthenticationInfo getAuthInfoFromToken(String token) {
        String json = new String(Base64.getDecoder()
                                       .decode(JWT.decode(token).getPayload()));
        Payload payload = new JWTParser().parsePayload(json);
        Map<String, Claim> claims = payload.getClaims();

        AuthenticationInfo authInfo = new AuthenticationInfo();
        authInfo.setId(claims.get("id").asInt());
        authInfo.setUsername(claims.get("username").asString());
        authInfo.setRoleName(claims.get("role").asString());
        authInfo.setPermissions(new HashSet<>(claims.get("permissions").asList(String.class)));

        return authInfo;
    }

    public boolean validateToken(String authToken) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey))
               .build()
               .verify(authToken);
            return true;
        } catch (JWTVerificationException | NullPointerException | IllegalArgumentException ex) {
            log.error("VALIDATE TOKEN FAILED: " + ex.getMessage());
            return false;
        }
    }

    public RefreshToken generateRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);

        userIdMap.fastPut(refreshToken.getToken(), user.getId(), 6, timeUnit);
        tokenMap.fastPut(user.getId(), user, 6, timeUnit);

        return refreshToken;
    }

    public RefreshToken verifyExpiration(String token) {
        if (userIdMap.remainTimeToLive(token) > 0) {
            Long userId = userIdMap.get(token);
            User user = tokenMap.get(userId);

            return generateRefreshToken(user);
        } else {
            throw new TokenExpiredException("Refresh token has been expired");
        }
    }

}
