package com.recceda.http.github;


import com.recceda.http.ApiPaths;
import com.recceda.http.Client;
import com.recceda.http.HttpConstants;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class GithubClient implements Client {
    private final HttpClient httpClient;
    private final String baseUrl;
    private final String token;

    public GithubClient(String token){
        this.token = token;
        this.baseUrl = ApiPaths.GITHUB_API_BASE_URL;
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    @Override
    public <T> CompletableFuture<HttpResponse<T>> send(HttpRequest request, HttpResponse.BodyHandler<T> bodyHandler) {
        return httpClient.sendAsync(request, bodyHandler);
    }

    @Override
    public HttpRequest.Builder requestBuilder(String path) {
        return HttpRequest.newBuilder()
                .uri(URI.create(this.baseUrl+path))
                .header("Authorization", HttpConstants.BEARER + HttpConstants.WHITESPACE + this.token);
    }
}
