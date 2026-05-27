package com.example.githubaskapp;


import java.util.List;

public record RepoResponse(String repositoryName, String ownerLogin, List<BranchInfo> branchName) {
}
