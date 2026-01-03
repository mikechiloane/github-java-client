package com.recceda.http.github;


import com.recceda.http.ApiPaths;
import com.recceda.http.Client;
import com.recceda.http.HttpConstants;
import com.recceda.http.RateLimit;
import com.recceda.http.RateLimitException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class GithubClient implements Client {
    private static final Logger logger = Logger.getLogger(GithubClient.class.getName());
    private final HttpClient httpClient;
    private final String baseUrl;
    private final String token;
    private boolean enableLogging;
    private volatile RateLimit currentRateLimit;
    private boolean enforceLimitCheck;

    public GithubClient(String token){
        this(token, false, true);
    }

    public GithubClient(String token, boolean enableLogging){
        this(token, enableLogging, true);
    }

    /**
     * Creates a new GitHub client
     * @param token GitHub API token
     * @param enableLogging Enable request/response logging
     * @param enforceLimitCheck If true, throws RateLimitException when limit is exceeded.
     *                          If false, only tracks and logs rate limits but allows requests.
     */
    public GithubClient(String token, boolean enableLogging, boolean enforceLimitCheck){
        this.token = token;
        this.baseUrl = ApiPaths.GITHUB_API_BASE_URL;
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.enableLogging = enableLogging;
        this.enforceLimitCheck = enforceLimitCheck;
        this.currentRateLimit = RateLimit.unlimited();
    }

    /**
     * Get the current rate limit information
     */
    public RateLimit getCurrentRateLimit() {
        return currentRateLimit;
    }

    /**
     * Check if the rate limit has been exceeded
     */
    public boolean isRateLimitExceeded() {
        return currentRateLimit.isExceeded();
    }

    @Override
    public <T> CompletableFuture<HttpResponse<T>> send(HttpRequest request, HttpResponse.BodyHandler<T> bodyHandler) {
        // Check rate limit before sending request
        if (enforceLimitCheck && currentRateLimit.isExceeded()) {
            logger.warning("Rate limit exceeded: " + currentRateLimit);
            return CompletableFuture.failedFuture(new RateLimitException(currentRateLimit));
        }

        if (enableLogging) {
            logger.info("Sending request: " + request.method() + " " + request.uri());
            logger.info("Current rate limit: " + currentRateLimit);
            request.headers().map().forEach((k, v) -> logger.info("  " + k + ": " + v));
        }

        return httpClient.sendAsync(request, bodyHandler)
                .whenComplete((response, throwable) -> {
                    if (response != null) {
                        // Update rate limit from response headers
                        updateRateLimitFromResponse(response);
                        
                        if (enableLogging) {
                            logger.info("Received response: Status " + response.statusCode());
                            logger.info("Updated rate limit: " + currentRateLimit);
                            response.headers().map().forEach((k, v) -> logger.info("  " + k + ": " + v));
                        }
                    } else if (throwable != null) {
                        if (enableLogging) {
                            logger.severe("Request failed: " + throwable.getMessage());
                        }
                    }
                });
    }

    /**
     * Update the rate limit information from response headers
     */
    private <T> void updateRateLimitFromResponse(HttpResponse<T> response) {
        try {
            var headers = response.headers();
            
            var limitOpt = headers.firstValue(HttpConstants.RATE_LIMIT_LIMIT);
            var remainingOpt = headers.firstValue(HttpConstants.RATE_LIMIT_REMAINING);
            var resetOpt = headers.firstValue(HttpConstants.RATE_LIMIT_RESET);
            
            if (limitOpt.isPresent() && remainingOpt.isPresent() && resetOpt.isPresent()) {
                int limit = Integer.parseInt(limitOpt.get());
                int remaining = Integer.parseInt(remainingOpt.get());
                Instant resetTime = Instant.ofEpochSecond(Long.parseLong(resetOpt.get()));
                
                this.currentRateLimit = new RateLimit(limit, remaining, resetTime);
                
                if (remaining <= 10) {
                    logger.warning(String.format("GitHub API rate limit running low: %d/%d remaining, resets in %d seconds",
                        remaining, limit, currentRateLimit.getSecondsUntilReset()));
                }
            }
        } catch (Exception e) {
            // If we can't parse rate limit headers, just log and continue
            if (enableLogging) {
                logger.warning("Failed to parse rate limit headers: " + e.getMessage());
            }
        }
    }

    @Override
    public HttpRequest.Builder requestBuilder(String path) {
        return HttpRequest.newBuilder()
                .uri(URI.create(this.baseUrl+path))
                .header("Authorization", HttpConstants.BEARER + HttpConstants.WHITESPACE + this.token);
    }
}
