package com.recceda.action;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.recceda.elements.Owner;
import com.recceda.http.Client;
import com.recceda.http.requests.user.UpdateUserRequest;
import com.recceda.mapper.RequestMapper;
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

    public  Owner getUser(String username) throws ExecutionException, InterruptedException, JsonProcessingException {
        HttpRequest request = client.requestBuilder(SLASH+USER+SLASH+username).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()).get();
        if(response.statusCode() != 200) throw new RuntimeException("User not found");
        return ResponseMapper.fromResponse(response, Owner.class);
    }

    public Owner getAuthenticatedUser() throws ExecutionException, InterruptedException, JsonProcessingException {
        HttpRequest request = client.requestBuilder(SLASH + USER).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()).get();
        return ResponseMapper.fromResponse(response, Owner.class);
    }

    public void starRepository(String path) throws ExecutionException, InterruptedException {
        HttpRequest request = client.requestBuilder(SLASH + USER + SLASH + STARRED + path)
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()).get();
        if (response.statusCode() != 204) throw new RuntimeException("Failed to star repo.");
    }

    public Owner updateAuthenticatedUser(UpdateUserRequest updateUserRequest) throws JsonProcessingException, ExecutionException, InterruptedException {
        HttpRequest request = client.requestBuilder(SLASH+USER)
                .method(PATCH, HttpRequest.BodyPublishers.ofString(RequestMapper.toJson(updateUserRequest)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()).get();
        return ResponseMapper.fromResponse(response, Owner.class);
    }

    public void followUser(String username) throws ExecutionException, InterruptedException {
        HttpRequest request = client.requestBuilder(SLASH + USER + FOLLOWING + SLASH + username)
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()).get();
        if (response.statusCode() != 204) throw new RuntimeException("Failed to follow user " + username);
    }

    public void unfollowUser(String username) throws ExecutionException, InterruptedException {
        HttpRequest request = client.requestBuilder(SLASH + USER + FOLLOWING + SLASH + username)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()).get();
        if (response.statusCode() != 204) throw new RuntimeException("Failed to unfollow user " + username);
    }
}
