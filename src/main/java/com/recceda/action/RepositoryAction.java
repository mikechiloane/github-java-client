package com.recceda.action;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.recceda.elements.Repository;
import com.recceda.http.Client;
import com.recceda.http.requests.file.CreateFileRequest;
import com.recceda.http.requests.repository.CreateRepositoryRequest;
import com.recceda.http.response.file.FileContentResponse;
import com.recceda.mapper.RequestMapper;
import com.recceda.mapper.ResponseMapper;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.recceda.Constants.*;

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
        HttpRequest request = client.requestBuilder(SLASH + REPOS + SLASH + owner + SLASH + name).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers
                .ofString()
        ).get();
        if (response.statusCode() != 200) return null;
        return ResponseMapper.fromResponse(response, Repository.class);
    }

    public List<Repository> getAllRepositoriesByOwner(final String owner) throws ExecutionException, InterruptedException, JsonProcessingException {
        HttpRequest request = client.requestBuilder(SLASH + USERS + SLASH + owner + SLASH + REPOS).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()).get();
        if (response.statusCode() != 200) List.of();
        return ResponseMapper.fromResponse(response, new TypeReference<List<Repository>>() {
        });
    }

    public Repository createRepositoryForAuthenticatedUser(CreateRepositoryRequest request) throws ExecutionException, InterruptedException, JsonProcessingException {
        HttpRequest req = client.requestBuilder(SLASH + USER + SLASH + REPOS)
                .POST(HttpRequest.BodyPublishers.ofString(RequestMapper.toJson(request)))
                .build();
        HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString()).get();
        if (response.statusCode() != 201) throw new RuntimeException("Failed to create repository.");
        return ResponseMapper.fromResponse(response, Repository.class);
    }

    public void deleteRepositoryForAuthenticatedUser(String owner, String name) throws ExecutionException, InterruptedException {
        HttpRequest request = client.requestBuilder(SLASH + REPOS + SLASH + owner + SLASH + name)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()).get();
        if (response.statusCode() != 204) {
            throw new RuntimeException("Failed to delete repository");
        }
    }

    public void createFile(CreateFileRequest createFileRequest, String owner, String repo, String path) throws JsonProcessingException, ExecutionException, InterruptedException {
        HttpRequest request = client.requestBuilder(SLASH + REPOS + SLASH + owner + SLASH + repo + SLASH + CONTENTS + SLASH + path)
                .PUT(HttpRequest.BodyPublishers.ofString(RequestMapper.toJson(createFileRequest)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString())
                .get();
        if (response.statusCode() != 201) throw new RuntimeException("Failed to create file.");
    }

    /**
     *
     * @param createFileRequest - this is the request to save the file
     * @param repository        - the repository which the file belongs to
     * @param path              - the path where the repository is saved to and
     * @throws JsonProcessingException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void createFile(CreateFileRequest createFileRequest, Repository repository, String path) throws JsonProcessingException, ExecutionException, InterruptedException {
        HttpRequest request = client.requestBuilder(SLASH + REPOS + SLASH + repository.getFullName() + SLASH + CONTENTS + SLASH + path)
                .PUT(HttpRequest.BodyPublishers.ofString(RequestMapper.toJson(createFileRequest)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString())
                .get();
        if (response.statusCode() != 201) throw new RuntimeException("Failed to create file.");
    }

    public void updateFile(CreateFileRequest createFileRequest, String owner, String repo, String path) throws JsonProcessingException, ExecutionException, InterruptedException {
        HttpRequest request = client.requestBuilder(SLASH + REPOS + SLASH + owner + SLASH + repo + SLASH + CONTENTS + SLASH + path)
                .PUT(HttpRequest.BodyPublishers.ofString(RequestMapper.toJson(createFileRequest)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString())
                .get();
        if (response.statusCode() != 200) throw new RuntimeException("Failed to create file.");
    }


    public <T> T getFileContents(String owner, String repo, String path, Class<T> type) throws ExecutionException, InterruptedException, JsonProcessingException {
        HttpRequest request = client.requestBuilder(SLASH + REPOS + SLASH + owner + SLASH + repo + SLASH + CONTENTS + SLASH + path)
                .header("Accept", GITHUB_RAW_JSON_HEADER)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()).get();
        if (response.statusCode() != 200) throw new RuntimeException("Failed to get file contents.");

        return ResponseMapper.fromResponse(response, type);
    }

    public FileContentResponse getFileContents(String owner, String repo, String path) throws ExecutionException, InterruptedException, JsonProcessingException {
        HttpRequest request = client.requestBuilder(SLASH + REPOS + SLASH + owner + SLASH + repo + SLASH + CONTENTS + SLASH + path)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()).get();
        if (response.statusCode() != 200) throw new RuntimeException("Failed to get file contents.");
        return ResponseMapper.fromResponse(response, FileContentResponse.class);
    }

}
