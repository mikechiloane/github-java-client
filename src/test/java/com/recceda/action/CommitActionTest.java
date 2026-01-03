package com.recceda.action;

import com.recceda.elements.Committer;
import com.recceda.http.github.GithubClient;
import com.recceda.http.requests.file.CreateFileRequest;
import com.recceda.http.requests.repository.CreateRepositoryRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CommitActionTest {

    private GithubClient githubClient;
    private RepositoryAction repositoryAction;
    private UserAction userAction;
    private FileAction fileAction;
    private CommitAction commitAction;

    @BeforeEach
    void setUp() throws Exception {
        githubClient = new GithubClient(System.getenv("API_TOKEN"));
        repositoryAction = new RepositoryAction(githubClient);
        userAction = new UserAction(githubClient);
        fileAction = new FileAction(githubClient);
        commitAction = new CommitAction(githubClient);
    }

    @Test
    void testGetCommit() throws Exception {
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
