package com.recceda.action;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.recceda.elements.Repository;
import com.recceda.http.ApiPaths;
import com.recceda.http.Client;
import com.recceda.http.HttpConstants;
import com.recceda.http.requests.file.CreateFileRequest;
import com.recceda.http.response.file.FileContentResponse;
import com.recceda.http.response.file.FileCreationResponse;
import com.recceda.mapper.RequestMapper;
import com.recceda.mapper.ResponseMapper;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

public class FileAction {

    private final Client client;

    public FileAction(Client client) {
        this.client = client;
    }

    public FileCreationResponse createFile(CreateFileRequest createFileRequest, String owner, String repo, String path) throws JsonProcessingException, ExecutionException, InterruptedException {
        return executeCreateOrUpdate(createFileRequest, owner, repo, path, 201, "Failed to create file.");
    }

    public FileCreationResponse createFile(CreateFileRequest createFileRequest, Repository repository, String path) throws JsonProcessingException, ExecutionException, InterruptedException {
        return createFile(createFileRequest, repository.getOwner().getLogin(), repository.getName(), path);
    }

    public FileCreationResponse updateFile(CreateFileRequest createFileRequest, String owner, String repo, String path) throws JsonProcessingException, ExecutionException, InterruptedException {
        return executeCreateOrUpdate(createFileRequest, owner, repo, path, 200, "Failed to update file.");
    }

    private FileCreationResponse executeCreateOrUpdate(CreateFileRequest createFileRequest, String owner, String repo, String path, int expectedStatusCode, String errorMessage) throws JsonProcessingException, ExecutionException, InterruptedException {
        HttpRequest request = client.requestBuilder(buildContentsPath(owner, repo, path))
                .PUT(HttpRequest.BodyPublishers.ofString(RequestMapper.toJson(createFileRequest)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString())
                .get();
        if (response.statusCode() != expectedStatusCode) throw new RuntimeException(errorMessage);
        return ResponseMapper.fromResponse(response, FileCreationResponse.class);
    }


    public <T> T getFileContents(String owner, String repo, String path, Class<T> type) throws ExecutionException, InterruptedException, JsonProcessingException {
        HttpRequest request = client.requestBuilder(buildContentsPath(owner, repo, path))
                .header("Accept", HttpConstants.GITHUB_RAW_JSON_HEADER)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()).get();
        if (response.statusCode() != 200) throw new RuntimeException("Failed to get file contents.");

        return ResponseMapper.fromResponse(response, type);
    }

    public <T> T getFileContents(Repository repository, String path, Class<T> type) throws ExecutionException, InterruptedException, JsonProcessingException {
        HttpRequest request = client.requestBuilder(buildContentsPath(repository.getOwner().getLogin(), repository.getName(), path))
                .header("Accept", HttpConstants.GITHUB_RAW_JSON_HEADER)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()).get();
        if (response.statusCode() != 200) throw new RuntimeException("Failed to get file contents.");

        return ResponseMapper.fromResponse(response, type);
    }

    public FileContentResponse getFileContents(String owner, String repo, String path) throws ExecutionException, InterruptedException, JsonProcessingException {
        HttpRequest request = client.requestBuilder(buildContentsPath(owner, repo, path))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()).get();
        if (response.statusCode() != 200) throw new RuntimeException("Failed to get file contents.");
        return ResponseMapper.fromResponse(response, FileContentResponse.class);
    }

    private String buildContentsPath(String owner, String repo, String path) {
        return ApiPaths.SLASH + ApiPaths.REPOS + ApiPaths.SLASH + owner + ApiPaths.SLASH + repo + ApiPaths.SLASH + ApiPaths.CONTENTS + ApiPaths.SLASH + path;
    }
}
