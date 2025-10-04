package com.recceda.http.github;


import com.recceda.http.Client;

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
        this.baseUrl = "https://api.github.com";
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getToken() {
        return token;
    }

    @Override
    public <T> CompletableFuture<HttpResponse<T>> send(HttpRequest request, HttpResponse.BodyHandler<T> bodyHandler) {
        return httpClient.sendAsync(request, bodyHandler);
    }

    @Override
    public HttpRequest.Builder requestBuilder(String path) {
        return HttpRequest.newBuilder()
                .uri(URI.create(this.baseUrl+path))
                .header("Authorization",this.token);
    }
}
