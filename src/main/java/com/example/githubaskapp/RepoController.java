package com.example.githubaskapp;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RepoController {

    private final RepoService repoService;

    public RepoController(RepoService repoService) {
        this.repoService = repoService;
    }


    @GetMapping("/users/{username}/repositories")
    public ResponseEntity<List<RepoResponse>> getRepos(@PathVariable String username) {
        List<RepoResponse> repositories = repoService.getRepositories(username);
        return ResponseEntity.ok(repositories);
    }
    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<ErrorResponse> handleException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(404, ex.getMessage()));
    }
}
