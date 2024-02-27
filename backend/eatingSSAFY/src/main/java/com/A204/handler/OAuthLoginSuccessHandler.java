package com.A204.handler;

import com.A204.domain.User;
import com.A204.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
@PropertySource("classpath:application.yaml")
public class OAuthLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UserService userService;

    @Value("${basic.url}")
    private String basicUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        // token->userId, nickname 추출
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        Map<String, Object> acountMap = token.getPrincipal().getAttributes();
        Long userId = Long.parseLong(token.getPrincipal().getAttribute("id").toString());
        String nickname = ((Map) ((Map) acountMap.get("kakao_account")).get("profile")).get("nickname").toString();

        if (userId == null) logger.error("bad userId");

        else {
            // user upsert
            User user = User.builder()
                    .id(userId)
                    .personNickname(nickname)
                    .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                    .build();
            userService.upsert(user);

            //쿠키에 userId 저장
            Cookie cookie = new Cookie("userId", userId.toString());
            cookie.setMaxAge(21600);
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        response.sendRedirect(basicUrl);
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
