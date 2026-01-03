package com.recceda.action;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.recceda.elements.Owner;
import com.recceda.http.github.GithubClient;
import com.recceda.http.requests.repository.CreateRepositoryRequest;
import com.recceda.http.requests.user.UpdateUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

class UserActionTest {

    GithubClient githubClient;
    Owner user;
    UserAction userAction;
    RepositoryAction repositoryAction;

    @BeforeEach
    void setUp() throws Exception {
        githubClient = new GithubClient(System.getenv("API_TOKEN"));
        userAction = new UserAction(githubClient);
        repositoryAction = new RepositoryAction(githubClient);
        user = userAction.getAuthenticatedUser();
    }

    @Test
    void testGetFollowers() throws ExecutionException, InterruptedException, JsonProcessingException {
        List<Owner> followers = userAction.getFollowersForUser("mikechiloane");
        assertTrue(followers.size()>0);
    }

    @Test
    void testUpdateUser() throws Exception {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .name("Enoch Sontonga")
                .bio("Dedicated Software Engineer")
                .build();
        Owner owner = userAction.updateAuthenticatedUser(request);
        assertNotNull(owner.getName());
        assertEquals(owner.getName(), request.getName());

    }


    @Test
    void testStarRepository() throws Exception {
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

    @Test
    void testFollowUser() throws ExecutionException, InterruptedException {
        userAction.followUser("google");
    }

    @Test
    void testUnfollowUser() throws ExecutionException, InterruptedException {
        userAction.unfollowUser("google");
    }
}