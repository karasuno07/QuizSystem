package com.fsoft.quizsystem.config.handler;

import com.fsoft.quizsystem.object.entity.jpa.User;
import com.fsoft.quizsystem.object.oauth2.CustomOAuth2User;
import com.fsoft.quizsystem.object.oauth2.OAuth2UserInfo;
import com.fsoft.quizsystem.object.oauth2.OAuth2UserInfoFactory;
import com.fsoft.quizsystem.service.JwtService;
import com.fsoft.quizsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final UserService userService;
    private final JwtService tokenService;

    @Value("${spring.security.oauth2.client.redirectUrl}")
    private String redirectUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        CustomOAuth2User oauth2User = (CustomOAuth2User) authentication.getPrincipal();
        String oauth2ClientName = oauth2User.getOauth2ClientName();

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                oauth2ClientName, oauth2User.getAttributes());

        User user;

        if (!userService.validateConcurrentEmail(oAuth2UserInfo.getEmail())) {
            user = userService.findUserByEmail(oAuth2UserInfo.getEmail());
        } else {
            user = userService.createUser(oAuth2UserInfo);
        }

        String token = tokenService.generateAccessToken(user);
        String refreshToken = tokenService.generateRefreshToken(user).getToken();

        redirectUrl = UriComponentsBuilder.fromUriString(redirectUrl)
                                        .queryParam("token", token)
                                        .queryParam("refreshToken", refreshToken)
                                        .queryParam("userId", user.getId())
                                        .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);

        super.onAuthenticationSuccess(request, response, authentication);
    }


}
