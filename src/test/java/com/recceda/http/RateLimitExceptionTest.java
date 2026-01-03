package com.recceda.http;

import org.junit.jupiter.api.Test;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class RateLimitExceptionTest {

    @Test
    void testExceptionCreation() {
        Instant resetTime = Instant.now().plusSeconds(3600);
        RateLimit rateLimit = new RateLimit(5000, 0, resetTime);
        
        RateLimitException exception = new RateLimitException(rateLimit);
        
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("rate limit exceeded"));
        assertEquals(rateLimit, exception.getRateLimit());
    }

    @Test
    void testExceptionWithCustomMessage() {
        Instant resetTime = Instant.now().plusSeconds(3600);
        RateLimit rateLimit = new RateLimit(5000, 0, resetTime);
        String customMessage = "Custom rate limit message";
        
        RateLimitException exception = new RateLimitException(customMessage, rateLimit);
        
        assertEquals(customMessage, exception.getMessage());
        assertEquals(rateLimit, exception.getRateLimit());
    }

    @Test
    void testExceptionMessageFormat() {
        Instant resetTime = Instant.now().plusSeconds(3600);
        RateLimit rateLimit = new RateLimit(5000, 10, resetTime);
        
        RateLimitException exception = new RateLimitException(rateLimit);
        String message = exception.getMessage();
        
        assertTrue(message.contains("10 requests remaining"));
        assertTrue(message.contains("seconds"));
    }
}
