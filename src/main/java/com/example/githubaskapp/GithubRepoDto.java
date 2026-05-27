package com.example.githubaskapp;

public record GithubRepoDto(String name,boolean fork,Owner owner) {
    record Owner(String login) {
    }
}
