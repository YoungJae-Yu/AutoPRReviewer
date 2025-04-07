package com.example.autoprreviewer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 허용할 URL 설정 (루트, 로그인 페이지 등은 누구나 접근 가능)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/login**", "/error**").permitAll()
                        .anyRequest().authenticated()
                )
                // OAuth2 로그인 설정 (기본 로그인 페이지 사용)
                .oauth2Login(Customizer.withDefaults());
        return http.build();
    }
}
