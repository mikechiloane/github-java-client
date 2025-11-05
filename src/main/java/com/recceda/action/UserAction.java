package com.recceda.action;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.recceda.elements.Owner;
import com.recceda.http.ApiPaths;
import com.recceda.http.Client;
import com.recceda.http.HttpConstants;
import com.recceda.http.requests.user.UpdateUserRequest;
import com.recceda.mapper.RequestMapper;
import com.recceda.mapper.ResponseMapper;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class UserAction {
    private final Client client;

    public UserAction(Client client) {
        this.client = client;
    }

    public Owner getUser(String username) throws ExecutionException, InterruptedException, JsonProcessingException {
        HttpRequest request = client.requestBuilder(ApiPaths.SLASH + ApiPaths.USERS + ApiPaths.SLASH + username).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()).get();
        if (response.statusCode() != 200) return null;
        return ResponseMapper.fromResponse(response, Owner.class);
    }

    public Owner getAuthenticatedUser() throws ExecutionException, InterruptedException, JsonProcessingException {
        HttpRequest request = client.requestBuilder(ApiPaths.SLASH + ApiPaths.USER).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()).get();
        return ResponseMapper.fromResponse(response, Owner.class);
    }

    public void starRepository(String owner, String repo) throws ExecutionException, InterruptedException {
        String path = ApiPaths.SLASH + ApiPaths.USER + ApiPaths.SLASH + ApiPaths.STARRED + ApiPaths.SLASH + owner + ApiPaths.SLASH + repo;
        HttpRequest request = client.requestBuilder(path)
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()).get();
        if (response.statusCode() != 204) throw new RuntimeException("Failed to star repo.");
    }

    public void unstarRepository(String owner, String repo) throws ExecutionException, InterruptedException {
        String path = ApiPaths.SLASH + ApiPaths.USER + ApiPaths.SLASH + ApiPaths.STARRED + ApiPaths.SLASH + owner + ApiPaths.SLASH + repo;
        HttpRequest request = client.requestBuilder(path)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()).get();
        if (response.statusCode() != 204) throw new RuntimeException("Failed to unstar repo.");
    }

    public Owner updateAuthenticatedUser(UpdateUserRequest updateUserRequest) throws JsonProcessingException, ExecutionException, InterruptedException {
        HttpRequest request = client.requestBuilder(ApiPaths.SLASH + ApiPaths.USER)
                .method(HttpConstants.PATCH, HttpRequest.BodyPublishers.ofString(RequestMapper.toJson(updateUserRequest)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()).get();
        return ResponseMapper.fromResponse(response, Owner.class);
    }

    public void followUser(String username) throws ExecutionException, InterruptedException {
        HttpRequest request = client.requestBuilder(ApiPaths.SLASH + ApiPaths.USER + ApiPaths.SLASH + ApiPaths.FOLLOWING + ApiPaths.SLASH + username)
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()).get();
        if (response.statusCode() != 204) throw new RuntimeException("Failed to follow user " + username);
    }

    public void unfollowUser(String username) throws ExecutionException, InterruptedException {
        HttpRequest request = client.requestBuilder(ApiPaths.SLASH + ApiPaths.USER + ApiPaths.SLASH + ApiPaths.FOLLOWING + ApiPaths.SLASH + username)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()).get();
        if (response.statusCode() != 204) throw new RuntimeException("Failed to unfollow user " + username);
    }

    public List<String> getFollowersForUser(String username) throws ExecutionException, InterruptedException, JsonProcessingException {

        HttpRequest request = client.requestBuilder(ApiPaths.SLASH+ApiPaths.USERS+ ApiPaths.SLASH+username+ApiPaths.SLASH+ApiPaths.FOLLOWERS).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()).get();
        if (response.statusCode() != 200) return null;
        return ResponseMapper.fromResponse(response, List.class);

    }
}
