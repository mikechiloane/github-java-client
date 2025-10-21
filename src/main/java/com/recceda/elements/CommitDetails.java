package com.recceda.elements;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CommitDetails {
    private GitUser author;
    private GitUser committer;
    private String message;
    private Tree tree;
    private String url;
    @JsonProperty("comment_count")
    private int commentCount;
    private Verification verification;
}
