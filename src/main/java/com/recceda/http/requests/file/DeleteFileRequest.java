package com.recceda.http.requests.file;

import com.recceda.elements.Committer;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteFileRequest {
    private String message;
    private Committer committer;
    private String sha;
}
