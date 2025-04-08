package com.example.autoprreviewer.service;

import com.example.autoprreviewer.integration.GitHubClient;
import com.example.autoprreviewer.model.RepositoryInfo;
import com.example.autoprreviewer.repository.RepositoryInfoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class RepositoryService {

    private final GitHubClient gitHubClient;
    private final RepositoryInfoRepository repositoryInfoRepository;

    public RepositoryService(GitHubClient gitHubClient, RepositoryInfoRepository repositoryInfoRepository) {
        this.gitHubClient = gitHubClient;
        this.repositoryInfoRepository = repositoryInfoRepository;
    }

    public Mono<List<Object>> fetchUserRepositories(String accessToken) {
        return gitHubClient.getUserRepositories(accessToken)
                .map(Arrays::asList);
    }

    public RepositoryInfo registerRepository(RepositoryInfo repositoryInfo) {
        return repositoryInfoRepository.save(repositoryInfo);
    }

    public Mono<String> registerWebhook(String accessToken, String owner, String repo, String webhookUrl) {
        return gitHubClient.registerWebhook(accessToken, owner, repo, webhookUrl);
    }

    public Mono<String> assignRandomReviewers(String accessToken, String owner, String repo, int prNumber) {
        // TODO: 실제 사용자 목록을 DB에서 불러오도록 확장 가능
        List<String> candidateReviewers = List.of(
                "YoungJae-Yu",
                "other-dev1",   // → 실제로 존재하는 GitHub 유저명으로 바꿔야 함!
                "other-dev2"
        );

        // 현재 PR 작성자는 제외해야 함 (전체에서 랜덤으로 2명 선택)
        Collections.shuffle(candidateReviewers);
        List<String> selected = candidateReviewers.subList(0, Math.min(2, candidateReviewers.size()));

        return gitHubClient.assignReviewers(accessToken, owner, repo, prNumber, selected);
    }


}
