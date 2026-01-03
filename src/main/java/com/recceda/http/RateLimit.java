package com.recceda.http;

import java.time.Instant;

/**
 * Represents GitHub API rate limit information.
 * GitHub provides rate limit information in response headers:
 * - X-RateLimit-Limit: The maximum number of requests per hour
 * - X-RateLimit-Remaining: The number of requests remaining
 * - X-RateLimit-Reset: The time when the rate limit resets (Unix timestamp)
 */
public class RateLimit {
    private final int limit;
    private final int remaining;
    private final Instant resetTime;

    public RateLimit(int limit, int remaining, Instant resetTime) {
        this.limit = limit;
        this.remaining = remaining;
        this.resetTime = resetTime;
    }

    /**
     * Creates a RateLimit instance with default values (no limit enforced)
     */
    public static RateLimit unlimited() {
        return new RateLimit(Integer.MAX_VALUE, Integer.MAX_VALUE, Instant.MAX);
    }

    /**
     * The maximum number of requests allowed per hour
     */
    public int getLimit() {
        return limit;
    }

    /**
     * The number of requests remaining in the current rate limit window
     */
    public int getRemaining() {
        return remaining;
    }

    /**
     * The time at which the current rate limit window resets
     */
    public Instant getResetTime() {
        return resetTime;
    }

    /**
     * Check if the rate limit has been exceeded
     */
    public boolean isExceeded() {
        return remaining <= 0 && Instant.now().isBefore(resetTime);
    }

    /**
     * Check if the rate limit has been reset
     */
    public boolean isReset() {
        return Instant.now().isAfter(resetTime);
    }

    /**
     * Get seconds until the rate limit resets
     */
    public long getSecondsUntilReset() {
        long seconds = resetTime.getEpochSecond() - Instant.now().getEpochSecond();
        return Math.max(0, seconds);
    }

    @Override
    public String toString() {
        return String.format("RateLimit{limit=%d, remaining=%d, resetsAt=%s (in %d seconds)}", 
            limit, remaining, resetTime, getSecondsUntilReset());
    }
}
