package io.github.williamandradesantana.sports.infrastructure.shared.apifootball;

import org.springframework.web.client.RestClient;

public class ApiFootballHttpClient {

    private final RestClient restClient;

    public ApiFootballHttpClient(ApiFootballProperties properties) {
        this.restClient = RestClient.builder()
                .baseUrl(properties.baseUrl())
                .defaultHeader("x-apisports-key", properties.apiKey())
                .build();
    }

    public RestClient getRestClient() {
        return restClient;
    }
}
