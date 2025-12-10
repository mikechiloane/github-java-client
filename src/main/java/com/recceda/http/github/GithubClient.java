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
import java.util.logging.Logger; // Import the Logger

public class GithubClient implements Client {
    private static final Logger logger = Logger.getLogger(GithubClient.class.getName()); // Logger instance
    private final HttpClient httpClient;
    private final String baseUrl;
    private final String token;
    private boolean enableLogging; // New field for logging

    public GithubClient(String token){
        this(token, false); // Call the new constructor with logging disabled by default
    }

    public GithubClient(String token, boolean enableLogging){ // New constructor
        this.token = token;
        this.baseUrl = ApiPaths.GITHUB_API_BASE_URL;
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.enableLogging = enableLogging; // Set the logging flag
    }

    @Override
    public <T> CompletableFuture<HttpResponse<T>> send(HttpRequest request, HttpResponse.BodyHandler<T> bodyHandler) {
        if (enableLogging) {
            logger.info("Sending request: " + request.method() + " " + request.uri());
            request.headers().map().forEach((k, v) -> logger.info("  " + k + ": " + v));
        }

        return httpClient.sendAsync(request, bodyHandler)
                .whenComplete((response, throwable) -> {
                    if (enableLogging) {
                        if (response != null) {
                            logger.info("Received response: Status " + response.statusCode());
                            response.headers().map().forEach((k, v) -> logger.info("  " + k + ": " + v));
                        } else if (throwable != null) {
                            logger.severe("Request failed: " + throwable.getMessage());
                        }
                    }
                });
    }

    @Override
    public HttpRequest.Builder requestBuilder(String path) {
        return HttpRequest.newBuilder()
                .uri(URI.create(this.baseUrl+path))
                .header("Authorization", HttpConstants.BEARER + HttpConstants.WHITESPACE + this.token);
    }
}
