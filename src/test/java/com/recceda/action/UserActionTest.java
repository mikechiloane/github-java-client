package com.recceda.action;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.recceda.elements.Owner;
import com.recceda.http.github.GithubClient;
import junit.framework.TestCase;

import java.util.concurrent.ExecutionException;

public class UserActionTest extends TestCase {

    GithubClient githubClient;
    Owner user;
    UserAction  userAction;

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


    public void testStarRepository() {

    }

    public void testFollowUser() {
    }

    public void testUnfollowUser() {
    }
}