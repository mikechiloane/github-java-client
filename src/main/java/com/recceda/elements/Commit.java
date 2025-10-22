package com.recceda.elements;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Commit {
    private String sha;
    @JsonProperty("node_id")
    private String nodeId;
    private CommitDetails commit;
    private String url;
    @JsonProperty("html_url")
    private String htmlUrl;
    @JsonProperty("comments_url")
    private String commentsUrl;
    private Owner author;
    private Owner committer;
    private List<Tree> parents;
}
