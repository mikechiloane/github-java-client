package com.recceda.http.requests.file;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.recceda.elements.Committer;
import com.recceda.encoder.ContentEncoder;
import com.recceda.mapper.RequestMapper;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateFileRequest {
    private String message;
    private Committer committer;
    private String content;
    private String sha;

    public CreateFileRequest(Object content, Committer committer, String message) throws JsonProcessingException {
        this.content = ContentEncoder.encodeToBase64(RequestMapper.toJson(content));
        this.committer = committer;
        this.message = message;
    }


}
