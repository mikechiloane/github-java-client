package com.recceda.action;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.recceda.elements.Owner;
import com.recceda.http.github.GithubClient;
import com.recceda.http.requests.user.UpdateUserRequest;
import junit.framework.TestCase;

import java.util.concurrent.ExecutionException;

public class UserActionTest extends TestCase {

    GithubClient githubClient;
    Owner user;
    UserAction userAction;

    public void setUp() throws Exception {
        super.setUp();
        githubClient = new GithubClient(System.getenv("API_TOKEN"));
        userAction = new UserAction(githubClient);
        user = userAction.getAuthenticatedUser();
    }

    public void testGetUser() throws ExecutionException, InterruptedException, JsonProcessingException {
        Owner owner = userAction.getAuthenticatedUser();
        assertNotNull(owner.getLogin());
        assertEquals(owner.getLogin(), this.user.getLogin());
    }

    public void testUpdateUser() throws Exception {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .name("Enoch Sontonga")
                .bio("Dedicated Software Engineer")
                .build();
        Owner owner = userAction.updateAuthenticatedUser(request);
        assertNotNull(owner.getName());
        assertEquals(owner.getName(), request.getName());

    }


    public void testStarRepository() {

    }

    public void testFollowUser() {
    }

    public void testUnfollowUser() {
    }
}