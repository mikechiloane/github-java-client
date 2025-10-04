package com.recceda.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import java.net.http.HttpResponse;

public class ResponseMapper {

    public static final ObjectMapper mapper = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    public static <T> T fromResponse(HttpResponse<String> response, Class<T> type) throws JsonProcessingException {
        return mapper.readValue(response.body(), type);
    }
    public static <T> T fromResponse(HttpResponse<String> response, TypeReference<T> typeRef) throws JsonProcessingException {
        return mapper.readValue(response.body(), typeRef);
    }
}
