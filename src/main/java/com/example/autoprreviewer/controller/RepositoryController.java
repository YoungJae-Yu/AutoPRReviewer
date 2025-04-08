package com.example.autoprreviewer.controller;

import com.example.autoprreviewer.model.RepositoryInfo;
import com.example.autoprreviewer.service.RepositoryService;
import com.example.autoprreviewer.util.OAuth2TokenUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/repositories")
public class RepositoryController {

    private final RepositoryService repositoryService;
    private final OAuth2TokenUtil tokenUtil;
    @Value("${webhook.github.url}")
    private String webhookUrl;

    public RepositoryController(RepositoryService repositoryService, OAuth2TokenUtil tokenUtil) {
        this.repositoryService = repositoryService;
        this.tokenUtil = tokenUtil;
    }

    // 레포지토리 목록 조회: 현재 로그인한 사용자의 GitHub 액세스 토큰을 사용
    @GetMapping("/list")
    public Mono<List<Object>> getRepositories(@AuthenticationPrincipal OAuth2User principal) {
        String accessToken = tokenUtil.getAccessToken();
        if (accessToken == null) {
            return Mono.error(new RuntimeException("액세스 토큰을 찾을 수 없습니다."));
        }
        return repositoryService.fetchUserRepositories(accessToken);
    }

    // DB에 레포지토리 등록 (클라이언트에서 선택한 정보를 받아서 저장)
    @PostMapping("/register")
    public RepositoryInfo registerRepository(@RequestBody RepositoryInfo repositoryInfo) {
        return repositoryService.registerRepository(repositoryInfo);
    }

    // Webhook 등록: 클라이언트가 요청할 때 호출 (예: 특정 레포에 대해)
    @PostMapping("/registerWebhook")
    public Mono<String> registerWebhook(
            @RequestParam String owner,
            @RequestParam String repo,
            @AuthenticationPrincipal OAuth2User principal
    ) {
        String accessToken = tokenUtil.getAccessToken();
        if (accessToken == null) {
            return Mono.error(new RuntimeException("액세스 토큰을 찾을 수 없습니다."));
        }
        return repositoryService.registerWebhook(accessToken, owner, repo, webhookUrl);
    }

}
