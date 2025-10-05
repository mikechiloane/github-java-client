package com.recceda.action;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.recceda.Constants.*;
import com.recceda.elements.Repository;
import com.recceda.http.Client;
import com.recceda.mapper.RequestMapper;
import com.recceda.mapper.ResponseMapper;
import com.recceda.requests.repository.CreateRepositoryRequest;

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
     * @param path
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws JsonProcessingException
     */
    public Repository getRepository(final String path) throws ExecutionException, InterruptedException, JsonProcessingException {
        HttpRequest request = client.requestBuilder(SLASH+REPOS+path).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers
                .ofString()
        ).get();
        return ResponseMapper.fromResponse(response, Repository.class);
    }

    public List<Repository> getAllRepositoriesByOwner(final String owner) throws ExecutionException, InterruptedException, JsonProcessingException {
        HttpRequest request = client.requestBuilder(SLASH+USERS+SLASH+owner+SLASH+REPOS).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()).get();
        System.out.println(response);
        return ResponseMapper.fromResponse(response, new TypeReference<List<Repository>>() {});
    }

    public void createRepository(CreateRepositoryRequest request) throws ExecutionException, InterruptedException, JsonProcessingException {
        HttpRequest req = client.requestBuilder(SLASH+USER+SLASH+REPOS)
                .POST(HttpRequest.BodyPublishers.ofString(RequestMapper.toJson(request)))
                .build();

        HttpResponse<String> response= client.send(req, HttpResponse.BodyHandlers.ofString()).get();
        System.out.println(response.body());
    }

    public void deleteRepository(String path) throws ExecutionException, InterruptedException {
        HttpRequest request = client.requestBuilder(SLASH+REPOS+SLASH+path)
                .DELETE()
                .build();

        HttpResponse<String>  response = client.send(request, HttpResponse.BodyHandlers.ofString()).get();
        if(response.statusCode() !=204){
            throw new RuntimeException("Failed to delete repository");
        }
    }


}
