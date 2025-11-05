package com.recceda.action;

import com.recceda.elements.Repository;
import com.recceda.http.github.GithubClient;
import com.recceda.http.requests.repository.CreateRepositoryRequest;
import junit.framework.TestCase;

import java.util.List;
import java.util.UUID;

public class RepositoryActionTest extends TestCase {

    private GithubClient githubClient;
    private RepositoryAction repositoryAction;
    private UserAction userAction;


    public void setUp() throws Exception {
        super.setUp();
        githubClient = new GithubClient(System.getenv("API_TOKEN"));
        repositoryAction = new RepositoryAction(githubClient);
        userAction = new UserAction(githubClient);
    }


    public void testGetRepository() throws Exception {
        var owner = userAction.getAuthenticatedUser();
        List<Repository> repositories = repositoryAction.getAllRepositoriesByOwner(owner.getLogin());
        assertNotNull(repositories);
        if (repositories.isEmpty()) {
            return;
        }
        var repositoryOne = repositories.getFirst();
        var repositoryTwo = repositoryAction.getRepository(repositoryOne.getOwner().getLogin(), repositoryOne.getName());
        assertNotNull(repositoryTwo);
        assertEquals(repositoryOne.getName(), repositoryTwo.getName());
    }


    public void testCreateRepositoryForAuthenticatedUser() throws Exception {
        var owner = userAction.getAuthenticatedUser();
        var repositoryName = UUID.randomUUID().toString();
        var repository = CreateRepositoryRequest.builder()
                .name(repositoryName)
                .description("description")
                .isPrivate(false)
                .build();
        Repository createdRepository = repositoryAction.createRepositoryForAuthenticatedUser(repository);
        assertNotNull(createdRepository);
        assertEquals(createdRepository.getName(), repositoryName);
        repositoryAction.deleteRepositoryForAuthenticatedUser(owner.getLogin(), repositoryName);
        var deletedRepository = repositoryAction.getRepository(owner.getLogin(), repositoryName);
        assertNull(deletedRepository);
    }
}