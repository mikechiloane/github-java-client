package com.recceda.http.response.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class FileContentResponse {
    private String name;
    private String path;
    private String sha;
    private int size;
    private String url;
    private String content;
}
