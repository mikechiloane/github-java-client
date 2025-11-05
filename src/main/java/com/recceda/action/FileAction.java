package com.recceda.action;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.recceda.elements.Repository;
import com.recceda.http.ApiPaths;
import com.recceda.http.Client;
import com.recceda.http.HttpConstants;
import com.recceda.http.requests.file.CreateFileRequest;
import com.recceda.http.requests.file.DeleteFileRequest;
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

    public FileCreationResponse createFile(CreateFileRequest createFileRequest, String owner, String repo, String path) {
        return executeCreateOrUpdate(createFileRequest, owner, repo, path, 201);
    }

    public FileCreationResponse createFile(CreateFileRequest createFileRequest, Repository repository, String path) {
        return createFile(createFileRequest, repository.getOwner().getLogin(), repository.getName(), path);
    }

    public FileCreationResponse updateFile(CreateFileRequest createFileRequest, String owner, String repo, String path) {
        return executeCreateOrUpdate(createFileRequest, owner, repo, path, 200);
    }

    public boolean deleteFile(DeleteFileRequest deleteFileRequest, String owner, String repo, String path) {
        try {
            HttpRequest request = client.requestBuilder(buildContentsPath(owner, repo, path))
                    .method("DELETE", HttpRequest.BodyPublishers.ofString(RequestMapper.toJson(deleteFileRequest)))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString())
                    .get();
            return response.statusCode() == 200;
        } catch (JsonProcessingException | ExecutionException | InterruptedException e) {
            return false;
        }
    }

    private FileCreationResponse executeCreateOrUpdate(CreateFileRequest createFileRequest, String owner, String repo, String path, int expectedStatusCode) {
        try {
            HttpRequest request = client.requestBuilder(buildContentsPath(owner, repo, path))
                    .PUT(HttpRequest.BodyPublishers.ofString(RequestMapper.toJson(createFileRequest)))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString())
                    .get();
            if (response.statusCode() != expectedStatusCode) return null;
            return ResponseMapper.fromResponse(response, FileCreationResponse.class);
        } catch (JsonProcessingException | ExecutionException | InterruptedException e) {
            return null;
        }
    }


    public <T> T getFileContents(String owner, String repo, String path, Class<T> type) {
        try {
            HttpRequest request = client.requestBuilder(buildContentsPath(owner, repo, path))
                    .header("Accept", HttpConstants.GITHUB_RAW_JSON_HEADER)
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()).get();
            if (response.statusCode() != 200) return null;

            return ResponseMapper.fromResponse(response, type);
        } catch (ExecutionException | InterruptedException | JsonProcessingException e) {
            return null;
        }
    }

    public FileContentResponse getFileContents(String owner, String repo, String path) {
        try {
            HttpRequest request = client.requestBuilder(buildContentsPath(owner, repo, path))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()).get();
            if (response.statusCode() != 200) return null;
            return ResponseMapper.fromResponse(response, FileContentResponse.class);
        } catch (ExecutionException | InterruptedException | JsonProcessingException e) {
            return null;
        }
    }

    private String buildContentsPath(String owner, String repo, String path) {
        return ApiPaths.SLASH + ApiPaths.REPOS + ApiPaths.SLASH + owner + ApiPaths.SLASH + repo + ApiPaths.SLASH + ApiPaths.CONTENTS + ApiPaths.SLASH + path;
    }
}
