package com.example.githubaskapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
class GitHubClient {
    private final RestClient restClient;

    GitHubClient(@Value("${github.api.base-url}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    List<GithubRepoDto> getRepo(String username) {



        return restClient.get()
                .uri("/users/{username}/repos", username)
                .retrieve()
                .onStatus(status -> status.value() == 404,
                        ((request, response) -> {throw new UserNotFoundException(username);}))
                .body(new ParameterizedTypeReference<List<GithubRepoDto>>() {
                });
    }

    List<GithubBranchDto> getBranches(String username, String repoName) {
        return restClient.get()
                .uri("/repos/{username}/{repoName}/branches", username, repoName)
                .retrieve()
                .body(new ParameterizedTypeReference<List<GithubBranchDto>>() {});
    }


}
