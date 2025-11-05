package com.recceda.action;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.recceda.elements.Owner;
import com.recceda.http.github.GithubClient;
import com.recceda.http.requests.repository.CreateRepositoryRequest;
import com.recceda.http.requests.user.UpdateUserRequest;
import junit.framework.TestCase;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class UserActionTest extends TestCase {

    GithubClient githubClient;
    Owner user;
    UserAction userAction;
    RepositoryAction repositoryAction;

    public void setUp() throws Exception {
        super.setUp();
        githubClient = new GithubClient(System.getenv("API_TOKEN"));
        userAction = new UserAction(githubClient);
        repositoryAction = new RepositoryAction(githubClient);
        user = userAction.getAuthenticatedUser();
    }

    public void testGetFollowers() throws ExecutionException, InterruptedException, JsonProcessingException {
        List<Owner> followers = userAction.getFollowersForUser("mikechiloane");
        assertTrue(followers.size()>0);
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


    public void testStarRepository() throws Exception {
        var owner = userAction.getAuthenticatedUser();
        var repositoryName = UUID.randomUUID().toString();
        var repository = CreateRepositoryRequest.builder()
                .name(repositoryName)
                .description("description")
                .isPrivate(false)
                .build();
        var createdRepository = repositoryAction.createRepositoryForAuthenticatedUser(repository);
        assertNotNull(createdRepository);
        userAction.starRepository(owner.getLogin(), repositoryName);
        userAction.unstarRepository(owner.getLogin(), repositoryName);
        repositoryAction.deleteRepositoryForAuthenticatedUser(owner.getLogin(), repositoryName);
    }

    public void testFollowUser() throws ExecutionException, InterruptedException {
        userAction.followUser("google");
    }

    public void testUnfollowUser() throws ExecutionException, InterruptedException {
        userAction.unfollowUser("google");
    }
}