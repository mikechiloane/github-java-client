package com.recceda.encoder;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ContentDecoder {

    public static String decodeFromBase64(String base64) {
        if (base64 == null || base64.isEmpty()) {
            throw new IllegalArgumentException("Input base64 content is empty");
        }
        byte[] decoded = Base64.getDecoder().decode(base64);
        return new String(decoded, StandardCharsets.UTF_8);
    }
}
