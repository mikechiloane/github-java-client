package com.recceda.http;

/**
 * Exception thrown when the GitHub API rate limit has been exceeded.
 */
public class RateLimitException extends RuntimeException {
    private final RateLimit rateLimit;

    public RateLimitException(RateLimit rateLimit) {
        super(String.format("GitHub API rate limit exceeded. %d requests remaining. Resets in %d seconds.", 
            rateLimit.getRemaining(), rateLimit.getSecondsUntilReset()));
        this.rateLimit = rateLimit;
    }

    public RateLimitException(String message, RateLimit rateLimit) {
        super(message);
        this.rateLimit = rateLimit;
    }

    public RateLimit getRateLimit() {
        return rateLimit;
    }
}
