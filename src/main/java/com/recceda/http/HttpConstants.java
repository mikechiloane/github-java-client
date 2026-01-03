package com.recceda.http;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class HttpConstants {
    public static final String BEARER = "Bearer";
    public static final String GITHUB_RAW_JSON_HEADER = "application/vnd.github.raw+json";
    public static final String PATCH = "PATCH";
    public static final String WHITESPACE = " ";
    
    // GitHub Rate Limit Headers
    public static final String RATE_LIMIT_LIMIT = "X-RateLimit-Limit";
    public static final String RATE_LIMIT_REMAINING = "X-RateLimit-Remaining";
    public static final String RATE_LIMIT_RESET = "X-RateLimit-Reset";
}
