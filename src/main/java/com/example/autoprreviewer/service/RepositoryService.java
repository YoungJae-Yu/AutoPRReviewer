package com.example.autoprreviewer.service;

import com.example.autoprreviewer.integration.GitHubClient;
import com.example.autoprreviewer.model.RepositoryInfo;
import com.example.autoprreviewer.repository.RepositoryInfoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Arrays;
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
}
