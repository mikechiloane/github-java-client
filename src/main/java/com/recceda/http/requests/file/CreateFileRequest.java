package com.recceda.http.requests.file;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.recceda.elements.Committer;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateFileRequest {
    private String message;
    private Committer committer;
    private String content;
    private String sha;
}
