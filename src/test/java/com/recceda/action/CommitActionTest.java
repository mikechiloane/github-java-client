package com.recceda.action;

import com.recceda.elements.Committer;
import com.recceda.http.github.GithubClient;
import com.recceda.http.requests.file.CreateFileRequest;
import com.recceda.http.requests.repository.CreateRepositoryRequest;
import junit.framework.TestCase;

import java.util.UUID;

public class CommitActionTest extends TestCase {

    private GithubClient githubClient;
    private RepositoryAction repositoryAction;
    private UserAction userAction;
    private FileAction fileAction;
    private CommitAction commitAction;

    public void setUp() throws Exception {
        super.setUp();
        githubClient = new GithubClient(System.getenv("API_TOKEN"));
        repositoryAction = new RepositoryAction(githubClient);
        userAction = new UserAction(githubClient);
        fileAction = new FileAction(githubClient);
        commitAction = new CommitAction(githubClient);
    }

    public void testGetCommit() throws Exception {
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

        var commit = commitAction.getCommit(owner.getLogin(), repositoryName, fileCreationResponse.getCommit().getSha());

        assertNotNull(commit);
        assertEquals(fileCreationResponse.getCommit().getSha(), commit.getSha());

        repositoryAction.deleteRepositoryForAuthenticatedUser(owner.getLogin(), repositoryName);
    }
}
