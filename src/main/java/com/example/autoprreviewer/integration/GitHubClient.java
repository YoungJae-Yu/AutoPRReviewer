package com.example.autoprreviewer.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GitHubClient {

    private final WebClient webClient;

    public GitHubClient(@Value("${github.api.base-url:https://api.github.com}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public Mono<Object[]> getUserRepositories(String accessToken) {
        return webClient.get()
                .uri("/user/repos")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(Object[].class);
    }

    public Mono<String> registerWebhook(String accessToken, String owner, String repo, String webhookUrl) {
        String url = "https://api.github.com/repos/" + owner + "/" + repo + "/hooks";

        WebClient webClient = WebClient.builder()
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github.v3+json")
                .build();

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "web");
        requestBody.put("active", true);
        requestBody.put("events", List.of("pull_request"));

        Map<String, String> config = new HashMap<>();
        config.put("url", webhookUrl); // ex: https://your-server.com/webhook/github
        config.put("content_type", "json");
        requestBody.put("config", config);

        return webClient.post()
                .uri(url)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> assignReviewers(String accessToken, String owner, String repo, int prNumber, List<String> reviewers) {
        String url = String.format("https://api.github.com/repos/%s/%s/pulls/%d/requested_reviewers", owner, repo, prNumber);

        Map<String, Object> requestBody = Map.of("reviewers", reviewers);

        return WebClient.builder()
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github+json")
                .build()
                .post()
                .uri(url)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class);
    }




    // 내부 DTO 클래스 - Webhook 등록을 위한 요청 바디
    public static class WebhookRequest {
        private String name = "web";
        private boolean active = true;
        private String[] events = {"pull_request"};
        private Config config;

        public WebhookRequest(String url) {
            this.config = new Config(url);
        }

        public static class Config {
            private String url;
            private String content_type = "json";

            public Config(String url) {
                this.url = url;
            }
            // getters/setters
            public String getUrl() {
                return url;
            }
            public void setUrl(String url) {
                this.url = url;
            }
            public String getContent_type() {
                return content_type;
            }
            public void setContent_type(String content_type) {
                this.content_type = content_type;
            }
        }
        // getters/setters
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public boolean isActive() {
            return active;
        }
        public void setActive(boolean active) {
            this.active = active;
        }
        public String[] getEvents() {
            return events;
        }
        public void setEvents(String[] events) {
            this.events = events;
        }
        public Config getConfig() {
            return config;
        }
        public void setConfig(Config config) {
            this.config = config;
        }
    }
}
