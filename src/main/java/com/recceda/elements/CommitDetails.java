package com.recceda.elements;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommitDetails {
    private GitUser author;
    private GitUser committer;
    private String message;
    private Tree tree;
    private String url;
    @JsonProperty("comment_count")
    private int commentCount;
}
