package com.example.autoprreviewer.controller;

import com.example.autoprreviewer.service.RepositoryService;
import com.example.autoprreviewer.util.OAuth2TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class WebhookController {

    private final RepositoryService repositoryService;
    private final OAuth2TokenUtil tokenUtil;

    @PostMapping("/github")
    public Mono<Void> handleGitHubWebhook(@RequestBody Map<String, Object> payload,
                                          @RequestHeader("X-GitHub-Event") String eventType) {

        log.info("📥 Webhook received: event = {}", eventType);

        // pull_request 이벤트만 처리
        if ("pull_request".equals(eventType)) {
            Map<String, Object> pullRequest = (Map<String, Object>) payload.get("pull_request");
            String action = (String) payload.get("action");

            if ("opened".equals(action)) {
                String prTitle = (String) pullRequest.get("title");
                Map<String, Object> repo = (Map<String, Object>) payload.get("repository");
                String fullName = (String) repo.get("full_name");
                String owner = fullName.split("/")[0];
                String repoName = fullName.split("/")[1];
                Integer prNumber = (Integer) payload.get("number");

                log.info("✨ PR opened: [{}] {} - PR #{}", owner, repoName, prNumber);

                String accessToken = tokenUtil.getAccessToken();
                return repositoryService.assignRandomReviewers(accessToken, owner, repoName, prNumber)
                        .doOnNext(res -> log.info("✅ 리뷰어 자동 등록 결과: {}", res))
                        .then(); // Mono<Void>
            }
        }

        return Mono.empty();
    }
}
