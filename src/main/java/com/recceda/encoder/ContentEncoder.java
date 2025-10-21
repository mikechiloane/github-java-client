package com.recceda.encoder;

import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@NoArgsConstructor
public class ContentEncoder {
    public static String encodeToBase64(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Input content is empty");
        }
        return Base64.getEncoder().encodeToString(input.getBytes(StandardCharsets.UTF_8));
    }

}
