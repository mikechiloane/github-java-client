package com.recceda.action;

import com.recceda.elements.Committer;
import com.recceda.elements.Owner;
import com.recceda.http.github.GithubClient;
import com.recceda.http.requests.file.CreateFileRequest;
import com.recceda.http.requests.repository.CreateRepositoryRequest;
import junit.framework.TestCase;

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
        var repositories = repositoryAction.getAllRepositoriesByOwner(owner.getLogin());
        assertNotNull(repositories);
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
                .isPrivate(false)
                .build();
        var createdRepository = repositoryAction.createRepositoryForAuthenticatedUser(repository);
        assertNotNull(createdRepository);
        assertEquals(createdRepository.getName(), repositoryName);
        repositoryAction.deleteRepositoryForAuthenticatedUser(owner.getLogin(), repositoryName);
        var deletedRepository = repositoryAction.getRepository(owner.getLogin(), repositoryName);
        assertNull(deletedRepository);
    }

    public void testCreateFile() throws Exception {
        var owner = userAction.getAuthenticatedUser();
        var repositoryName = UUID.randomUUID().toString();
        var fileName = UUID.randomUUID().toString() + ".txt";

        var createRepositoryRequest = CreateRepositoryRequest.builder()
                .name(repositoryName)
                .description("description")
                .isPrivate(false)
                .build();
        repositoryAction.createRepositoryForAuthenticatedUser(createRepositoryRequest);

        var createFileRequest = new CreateFileRequest(owner, Committer.builder().email(owner.getLogin()).name(repositoryName).build(), fileName);

        repositoryAction.createFile(createFileRequest, owner.getLogin(), repositoryName, fileName);

        var fileContents = repositoryAction.getFileContents(owner.getLogin(), repositoryName, fileName);
        assertNotNull(fileContents);
        assertEquals(fileName, fileContents.getName());


        repositoryAction.deleteRepositoryForAuthenticatedUser(owner.getLogin(), repositoryName);
    }

    public void testUpdateFile() throws Exception {
        var owner = userAction.getAuthenticatedUser();
        var repositoryName = UUID.randomUUID().toString();
        var fileName = UUID.randomUUID().toString() + ".txt";

        var createRepositoryRequest = CreateRepositoryRequest.builder()
                .name(repositoryName)
                .description("description")
                .isPrivate(false)
                .build();
        repositoryAction.createRepositoryForAuthenticatedUser(createRepositoryRequest);
        var createFileRequest = new CreateFileRequest(owner, Committer.builder().email(owner.getLogin()).name(repositoryName).build(), fileName);
        repositoryAction.createFile(createFileRequest, owner.getLogin(), repositoryName, fileName);

        var fileContentsBeforeUpdate = repositoryAction.getFileContents(owner.getLogin(), repositoryName, fileName, Owner.class);
        assertNotNull(fileContentsBeforeUpdate);
        assertEquals(owner.getLogin(), fileContentsBeforeUpdate.getLogin());

        var fileSha = repositoryAction.getFileContents(owner.getLogin(), repositoryName, fileName).getSha();

        owner.setName("New Name");
        CreateFileRequest newUpdateReqeust = new CreateFileRequest(owner, Committer.builder().email(owner.getLogin()).name(repositoryName).build(), fileName);
        newUpdateReqeust.setSha(fileSha);
        repositoryAction.updateFile(newUpdateReqeust, owner.getLogin(), repositoryName, fileName);
        var fileContentsAfterUpdate = repositoryAction.getFileContents(owner.getLogin(), repositoryName, fileName, Owner.class);
        assertNotNull(fileContentsAfterUpdate);
        assertEquals(owner.getName(), fileContentsAfterUpdate.getName());

        repositoryAction.deleteRepositoryForAuthenticatedUser(owner.getLogin(), repositoryName);

    }


}