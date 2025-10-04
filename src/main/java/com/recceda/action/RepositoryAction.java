package com.recceda.action;

import com.recceda.Endpoints;
import com.recceda.elements.Repository;
import com.recceda.http.Client;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class RepositoryAction {
    private final Client client;


    public RepositoryAction(Client client) {
        this.client = client;
    }

    public Optional<Repository> getRepository(String path) {
        HttpRequest request = client.requestBuilder(Endpoints.REPOS+path).build();
        var res = client.send(request, HttpResponse.BodyHandlers
                .ofString()
        );
        System.out.println(res.join());
        return Optional.of(new Repository());
    }

}
