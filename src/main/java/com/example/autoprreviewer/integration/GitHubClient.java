package com.example.autoprreviewer.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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

    // Webhook 등록 메서드 예시
    public Mono<String> registerWebhook(String accessToken, String owner, String repo, String webhookUrl) {
        // POST /repos/{owner}/{repo}/hooks
        // Payload 예시:
        /*
          {
             "name": "web",
             "active": true,
             "events": ["pull_request"],
             "config": {
                 "url": "https://your-webhook-url.com/github/webhook",
                 "content_type": "json"
             }
          }
         */
        return webClient.post()
                .uri("/repos/{owner}/{repo}/hooks", owner, repo)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .bodyValue(new WebhookRequest(webhookUrl))
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
