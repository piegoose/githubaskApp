package com.example.githubaskapp;

record GithubBranchDto(String name, Commit commit) {
    record Commit(String sha) {}
}