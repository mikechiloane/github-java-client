package com.recceda.action;

import com.recceda.Endpoints;
import com.recceda.Entpoints;
import com.recceda.elements.Repository;
import com.recceda.http.Client;

import java.net.http.HttpRequest;
import java.util.Optional;

public class RepositoryAction {
    private final Client client;

    public RepositoryAction(Client client) {
        this.client = client;
    }

    public Optional<Repository> getRepository(String name) {
        HttpRequest request = client.requestBuilder(Endpoints.REPOS+name).build();
        client.send(request);
        return null;
    }

}
