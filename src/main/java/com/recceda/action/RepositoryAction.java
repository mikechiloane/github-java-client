package com.recceda.action;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.recceda.elements.Owner;
import com.recceda.elements.Repository;
import com.recceda.http.ApiPaths;
import com.recceda.http.Client;
import com.recceda.http.requests.repository.CreateRepositoryRequest;
import com.recceda.mapper.RequestMapper;
import com.recceda.mapper.ResponseMapper;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RepositoryAction {
    private final Client client;


    public RepositoryAction(Client client) {
        this.client = client;
    }

    /**
     * Return repository data for a repository path
     *
     * @param owner - owner o the repository
     * @param name  - name of the repository
     * @return Repository
     */
    public Repository getRepository(final String owner, final String name) throws ExecutionException, InterruptedException, JsonProcessingException {
        HttpRequest request = client.requestBuilder(ApiPaths.SLASH + ApiPaths.REPOS + ApiPaths.SLASH + owner + ApiPaths.SLASH + name).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers
                .ofString()
        ).get();
        if (response.statusCode() != 200) return null;
        return ResponseMapper.fromResponse(response, Repository.class);
    }

    public List<Repository> getAllRepositoriesByOwner(final String owner) throws ExecutionException, InterruptedException, JsonProcessingException {
        HttpRequest request = client.requestBuilder(ApiPaths.SLASH + ApiPaths.USERS + ApiPaths.SLASH + owner + ApiPaths.SLASH + ApiPaths.REPOS).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()).get();
        if (response.statusCode() != 200) List.of();
        return ResponseMapper.fromResponse(response, new TypeReference<List<Repository>>() {
        });
    }

    public Repository createRepositoryForAuthenticatedUser(CreateRepositoryRequest request) throws ExecutionException, InterruptedException, JsonProcessingException {
        HttpRequest req = client.requestBuilder(ApiPaths.SLASH + ApiPaths.USER + ApiPaths.SLASH + ApiPaths.REPOS)
                .POST(HttpRequest.BodyPublishers.ofString(RequestMapper.toJson(request)))
                .build();
        HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString()).get();
        if (response.statusCode() != 201) return null;
        return ResponseMapper.fromResponse(response, Repository.class);
    }

    public void deleteRepositoryForAuthenticatedUser(String owner, String name) throws ExecutionException, InterruptedException {
        HttpRequest request = client.requestBuilder(ApiPaths.SLASH + ApiPaths.REPOS + ApiPaths.SLASH + owner + ApiPaths.SLASH + name)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()).get();
        if (response.statusCode() != 204) {
            throw new RuntimeException("Failed to delete repository");
        }
    }

    public List<Owner> getStargazersFor(String owner, String repo) throws JsonProcessingException, ExecutionException, InterruptedException {
        HttpRequest request = client.requestBuilder(ApiPaths.SLASH + ApiPaths.REPOS + ApiPaths.SLASH + owner + ApiPaths.SLASH + repo + ApiPaths.SLASH + ApiPaths.STARGAZERS).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()).get();
        if (response.statusCode() != 200) return null;
        return ResponseMapper.fromResponse(response, new TypeReference<>() {
        });
    }
}
