package com.recceda.action;

import com.recceda.elements.Committer;
import com.recceda.elements.Owner;
import com.recceda.http.github.GithubClient;
import com.recceda.http.requests.file.CreateFileRequest;
import com.recceda.http.requests.file.DeleteFileRequest;
import com.recceda.http.requests.repository.CreateRepositoryRequest;
import junit.framework.TestCase;

import java.util.UUID;

public class FileActionTest extends TestCase {

    private GithubClient githubClient;
    private RepositoryAction repositoryAction;
    private UserAction userAction;
    private FileAction fileAction;


    public void setUp() throws Exception {
        super.setUp();
        githubClient = new GithubClient(System.getenv("API_TOKEN"));
        repositoryAction = new RepositoryAction(githubClient);
        userAction = new UserAction(githubClient);
        fileAction = new FileAction(githubClient);
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

        var fileCreationResponse = fileAction.createFile(createFileRequest, owner.getLogin(), repositoryName, fileName);

        assertNotNull(fileCreationResponse);
        assertNotNull(fileCreationResponse.getContent());
        assertEquals(fileName, fileCreationResponse.getContent().getName());


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
        var fileCreationResponse = fileAction.createFile(createFileRequest, owner.getLogin(), repositoryName, fileName);

        var fileContentsBeforeUpdate = fileAction.getFileContents(owner.getLogin(), repositoryName, fileName, Owner.class);
        assertNotNull(fileContentsBeforeUpdate);
        assertEquals(owner.getLogin(), fileContentsBeforeUpdate.getLogin());

        var fileSha = fileCreationResponse.getContent().getSha();

        owner.setName("New Name");
        CreateFileRequest newUpdateReqeust = new CreateFileRequest(owner, Committer.builder().email(owner.getLogin()).name(repositoryName).build(), fileName);
        newUpdateReqeust.setSha(fileSha);
        var updatedFileResponse = fileAction.updateFile(newUpdateReqeust, owner.getLogin(), repositoryName, fileName);
        var fileContentsAfterUpdate = fileAction.getFileContents(owner.getLogin(), repositoryName, fileName, Owner.class);
        assertNotNull(fileContentsAfterUpdate);
        assertEquals(owner.getName(), fileContentsAfterUpdate.getName());

        repositoryAction.deleteRepositoryForAuthenticatedUser(owner.getLogin(), repositoryName);

    }

    public void testDeleteFile() throws Exception {
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
        var fileCreationResponse = fileAction.createFile(createFileRequest, owner.getLogin(), repositoryName, fileName);

        var fileSha = fileCreationResponse.getContent().getSha();

        var deleteFileRequest = DeleteFileRequest.builder()
                .message("Deleting test file")
                .sha(fileSha)
                .committer(Committer.builder().email(owner.getLogin()).name(owner.getLogin()).build())
                .build();

        fileAction.deleteFile(deleteFileRequest, owner.getLogin(), repositoryName, fileName);

        try {
            fileAction.getFileContents(owner.getLogin(), repositoryName, fileName);
            fail("Expected RuntimeException for file not found");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("Failed to get file contents."));
        }

        repositoryAction.deleteRepositoryForAuthenticatedUser(owner.getLogin(), repositoryName);
    }


}