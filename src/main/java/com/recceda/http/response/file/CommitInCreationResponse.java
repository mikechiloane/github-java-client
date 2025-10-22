package com.recceda.http.response.file;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.recceda.elements.GitUser;
import com.recceda.elements.Tree;
import com.recceda.elements.Verification;
import lombok.Data;

import java.util.List;

@Data
public class CommitInCreationResponse {
    private String sha;
    @JsonProperty("node_id")
    private String nodeId;
    private String url;
    @JsonProperty("html_url")
    private String htmlUrl;
    private GitUser author;
    private GitUser committer;
    private Tree tree;
    private String message;
    private List<Tree> parents;
    private Verification verification;
}
