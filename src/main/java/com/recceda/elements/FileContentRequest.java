package com.recceda.elements;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class FileContentRequest {
    private String message;
    private Committer committer;
    private String content;
}
