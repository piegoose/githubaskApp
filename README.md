# GitHub Repositories API

Simple REST API built with Java 25 and Spring Boot 4 that acts as a proxy for the GitHub API.

The application allows consumers to fetch all non-fork GitHub repositories for a given user together with branch information and last commit SHA.

## Technologies

- Java 25
- Spring Boot 4
- Gradle Kotlin DSL
- WireMock
- JUnit 5

## Features

- List all non-fork repositories for a GitHub user
- Return repository branches with last commit SHA
- Handle non-existing GitHub users with custom 404 response
- Integration tests using WireMock

## Endpoint

### Get repositories

```http
GET /users/{username}/repositories
```

### Example request

```http
GET /users/piegoose/repositories
```

### Example response

```json
{
  "repositoryName": "askAI",
  "ownerLogin": "piegoose",
  "branchName": [
    {
      "name": "main",
      "lastCommitSha": "77e093b4d0155f0829140ef813e76acb7e7b0f5d"
    }
  ]
}
```

## Error handling

If the GitHub user does not exist, the application returns:

```json
{
  "status": 404,
  "message": "Could not find user piegoose1"
}
```

## Running the application

### Clone repository

```bash
git clone <repository-url>
```

### Build project

```bash
./gradlew build
```

### Run application

```bash
./gradlew bootRun
```

Application starts on:

```text
http://localhost:8080
```

## Running tests

```bash
./gradlew test
```

## GitHub API

The application uses GitHub REST API v3:

- `GET /users/{username}/repos`
- `GET /repos/{owner}/{repo}/branches`

Documentation:

https://developer.github.com/v