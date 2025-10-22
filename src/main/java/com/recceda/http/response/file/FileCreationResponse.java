package com.recceda.http.response.file;

import lombok.Data;

@Data
public class FileCreationResponse {
    private FileContentResponse content;
    private CommitInCreationResponse commit;
}
