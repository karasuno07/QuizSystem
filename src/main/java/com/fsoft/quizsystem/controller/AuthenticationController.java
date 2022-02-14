package com.fsoft.quizsystem.controller;

import com.fsoft.quizsystem.object.dto.mapper.UserMapper;
import com.fsoft.quizsystem.object.dto.request.AuthenticationRequest;
import com.fsoft.quizsystem.object.dto.response.TokenResponse;
import com.fsoft.quizsystem.object.dto.response.UserResponse;
import com.fsoft.quizsystem.object.entity.User;
import com.fsoft.quizsystem.object.model.RefreshToken;
import com.fsoft.quizsystem.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Log4j2
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtService tokenService;
    private final UserMapper userMapper;

    /* username: admin, password: 123456 */
    @PostMapping(value = "login")
    ResponseEntity<?> createAccessToken(@RequestBody AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
   
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();

        String accessToken = tokenService.generateAccessToken(user);
        RefreshToken refreshToken = tokenService.generateRefreshToken(user);

        TokenResponse response = new TokenResponse(accessToken, refreshToken.getToken(),
                                                   userMapper.entityToUserResponse(user));;

        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "refresh-token/{token}")
    ResponseEntity<?> refreshAccessToken(@PathVariable String token) {
        RefreshToken refreshToken = tokenService.verifyExpiration(token);

        String newAccessToken = tokenService.generateAccessToken(refreshToken.getUser());
        UserResponse userInfo = userMapper.entityToUserResponse(refreshToken.getUser());

        TokenResponse response = new TokenResponse(newAccessToken,
                                                   refreshToken.getToken(), userInfo);

        return ResponseEntity.ok(response);
    }
}
