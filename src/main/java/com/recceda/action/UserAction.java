package com.recceda.action;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.recceda.elements.Owner;
import com.recceda.http.Client;
import com.recceda.mapper.ResponseMapper;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

import static com.recceda.Constants.*;

public class UserAction {
    private final Client client;

    public UserAction(Client client) {
        this.client = client;
    }

    public Owner getAuthenticatedUser() throws ExecutionException, InterruptedException, JsonProcessingException {
        HttpRequest request = client.requestBuilder(SLASH + USER).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()).get();
        return ResponseMapper.fromResponse(response, Owner.class);
    }

    public void starRepository(String path) throws ExecutionException, InterruptedException {
        HttpRequest request = client.requestBuilder(SLASH + USER + SLASH + STARRED + path).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()).get();
        if (response.statusCode() != 204) throw new RuntimeException("Failed to star repo");
    }
}
