package io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.shared;

import io.github.williamandradesantana.sports.infrastructure.shared.apifootball.ApiFootballHttpClient;
import io.github.williamandradesantana.sports.infrastructure.shared.apifootball.ApiFootballProperties;
import org.springframework.web.client.RestClient;

public class TestApiFootballHttpClient extends ApiFootballHttpClient {
   private final RestClient restClient;

    public TestApiFootballHttpClient(RestClient restClient) {
        super(new ApiFootballProperties("http://unused", "unused"));
        this.restClient = restClient;
    }

    @Override
    public RestClient getRestClient() {
        return restClient;
    }
}
