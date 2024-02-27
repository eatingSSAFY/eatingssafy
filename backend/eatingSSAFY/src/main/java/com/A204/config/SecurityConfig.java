package com.A204.config;

import com.A204.handler.OAuthLoginFailureHandler;
import com.A204.handler.OAuthLoginSuccessHandler;
import com.A204.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserService userService;
    private final OAuthLoginSuccessHandler oAuthLoginSuccessHandler;
    private final OAuthLoginFailureHandler oAuthLoginFailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        //todo: front에 login 페이지 만들어지면, .loginPage("${url}") 추가할 것
        httpSecurity.csrf().disable()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(userService)
                .and()
                .successHandler(oAuthLoginSuccessHandler)
                .failureHandler(oAuthLoginFailureHandler);
        return httpSecurity.build();
    }
}
