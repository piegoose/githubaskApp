package com.example.githubaskapp;


import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RepoService {
    private final GitHubClient gitHubClient;

    public RepoService(GitHubClient gitHubClient) {
        this.gitHubClient = gitHubClient;
    }

    List<RepoResponse> getRepositories(String name) {
        return gitHubClient.getRepo(name).stream()
            .filter(repo -> !repo.fork())
            .map(repo -> new RepoResponse(
                    repo.name(),
                    repo.owner().login(),
                    gitHubClient.getBranches(name, repo.name())
                            .stream()
                            .map(branch -> new BranchInfo(branch.name(), branch.commit().sha()))
                            .toList()
            ))
            .toList();
    }

}