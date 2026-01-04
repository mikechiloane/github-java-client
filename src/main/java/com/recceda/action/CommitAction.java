package com.recceda.action;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.recceda.elements.Commit;
import com.recceda.http.ApiPaths;
import com.recceda.http.Client;
import com.recceda.mapper.ResponseMapper;
import lombok.Getter;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

public class CommitAction {
    @Getter
    private final Client client;

    public CommitAction(Client client) {
        this.client = client;
    }

    public Commit getCommit(String owner, String repo, String sha) throws ExecutionException, InterruptedException, JsonProcessingException {
        String path = ApiPaths.SLASH + ApiPaths.REPOS + ApiPaths.SLASH + owner + ApiPaths.SLASH + repo + ApiPaths.SLASH + ApiPaths.COMMITS + ApiPaths.SLASH + sha;
        HttpRequest request = client.requestBuilder(path).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()).get();
        if (response.statusCode() != 200) {
            return null;
        }
        return ResponseMapper.fromResponse(response, Commit.class);
    }
}
