package com.recceda.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

public class RequestMapper {

    private static final ObjectMapper mapper = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    private RequestMapper() {}

    public static String toJson(Object request) throws JsonProcessingException {
        return mapper.writeValueAsString(request);
    }
}
