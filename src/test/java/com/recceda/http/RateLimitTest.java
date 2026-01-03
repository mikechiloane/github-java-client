package com.recceda.http;

import org.junit.jupiter.api.Test;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class RateLimitTest {

    @Test
    void testRateLimitCreation() {
        Instant resetTime = Instant.now().plusSeconds(3600);
        RateLimit rateLimit = new RateLimit(5000, 4950, resetTime);
        
        assertEquals(5000, rateLimit.getLimit());
        assertEquals(4950, rateLimit.getRemaining());
        assertEquals(resetTime, rateLimit.getResetTime());
    }

    @Test
    void testUnlimitedRateLimit() {
        RateLimit unlimited = RateLimit.unlimited();
        
        assertFalse(unlimited.isExceeded());
        assertEquals(Integer.MAX_VALUE, unlimited.getLimit());
        assertEquals(Integer.MAX_VALUE, unlimited.getRemaining());
    }

    @Test
    void testIsExceeded() {
        Instant futureReset = Instant.now().plusSeconds(3600);
        RateLimit exceededLimit = new RateLimit(5000, 0, futureReset);
        
        assertTrue(exceededLimit.isExceeded());
    }

    @Test
    void testIsNotExceeded() {
        Instant futureReset = Instant.now().plusSeconds(3600);
        RateLimit validLimit = new RateLimit(5000, 100, futureReset);
        
        assertFalse(validLimit.isExceeded());
    }

    @Test
    void testIsReset() {
        Instant pastReset = Instant.now().minusSeconds(60);
        RateLimit resetLimit = new RateLimit(5000, 0, pastReset);
        
        assertTrue(resetLimit.isReset());
        assertFalse(resetLimit.isExceeded());
    }

    @Test
    void testSecondsUntilReset() {
        Instant futureReset = Instant.now().plusSeconds(3600);
        RateLimit rateLimit = new RateLimit(5000, 100, futureReset);
        
        long seconds = rateLimit.getSecondsUntilReset();
        assertTrue(seconds > 3590 && seconds <= 3600);
    }

    @Test
    void testSecondsUntilResetWhenAlreadyReset() {
        Instant pastReset = Instant.now().minusSeconds(60);
        RateLimit rateLimit = new RateLimit(5000, 0, pastReset);
        
        assertEquals(0, rateLimit.getSecondsUntilReset());
    }

    @Test
    void testToString() {
        Instant resetTime = Instant.now().plusSeconds(3600);
        RateLimit rateLimit = new RateLimit(5000, 4950, resetTime);
        
        String str = rateLimit.toString();
        assertTrue(str.contains("limit=5000"));
        assertTrue(str.contains("remaining=4950"));
    }
}
