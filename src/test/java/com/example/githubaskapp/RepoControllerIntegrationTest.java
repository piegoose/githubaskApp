package com.example.githubaskapp;


import com.github.tomakehurst.wiremock.WireMockServer;
import jdk.jfr.Name;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static com.github.tomakehurst.wiremock.client.WireMock.get;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "github.api.base-url=http://localhost:8089")
public class RepoControllerIntegrationTest {

    static WireMockServer wireMockServer = new WireMockServer(8089);

    @BeforeAll
    static void setUp() {
        wireMockServer.start();
    }

    @AfterAll
    static void tearDown() {
        wireMockServer.stop();
    }

    @LocalServerPort
    int port;

    RestTemplate restTemplate = new RestTemplate();
    {
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(HttpStatusCode statusCode) {
                return false;
            }
        });
    }

    @Test
    @Name("should return non fork repositories with branches")
    void should_return_non_fork_repositories_with_branches(){
        wireMockServer.stubFor(get(urlEqualTo("/users/testuser/repos"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
            [
                {"name": "normalRepo", "fork": false, "owner": {"login": "testuser"}},
                {"name": "forkedRepo", "fork": true, "owner": {"login": "testuser"}}
            ]
        """)));

        wireMockServer.stubFor(get(urlEqualTo("/repos/testuser/normalRepo/branches"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
            [
                {"name": "main", "commit": {"sha": "abc123"}}
            ]
        """)));
        var response = restTemplate.getForEntity(
                "http://localhost:" + port + "/users/testuser/repositories",
                RepoResponse[].class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody()[0].repositoryName()).isEqualTo("normalRepo");
    }
    @Test
    @Name("should return 404 when user not found")
    void should_return_404_when_user_not_found(){
        wireMockServer.stubFor(get(urlEqualTo("/users/nonexistentuser/repos"))
                .willReturn(aResponse()
                        .withStatus(404)));

        var response = restTemplate.getForEntity(
                "http://localhost:" + port + "/users/nonexistentuser/repositories",
                ErrorResponse.class);

        assertThat(response.getStatusCode().value()).isEqualTo(404);
        assertThat(response.getBody().status()).isEqualTo(404);
        assertThat(response.getBody().message()).isNotBlank();
    }
}