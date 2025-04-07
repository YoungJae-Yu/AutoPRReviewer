package com.example.autoprreviewer.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home(@AuthenticationPrincipal OAuth2User principal) {
        if (principal != null) {
            // GitHub에서 가져오는 기본 사용자 속성은 'login'
            return "Welcome, " + principal.getAttribute("login") + "!";
        }
        return "Welcome, Guest! <a href='/oauth2/authorization/github'>Login with GitHub</a>";
    }
}
