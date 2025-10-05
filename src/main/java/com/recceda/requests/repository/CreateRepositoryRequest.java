package com.recceda.requests.repository;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateRepositoryRequest {
    private String name;
    private String description;
    private String homepage;
    @JsonProperty("private")
    private boolean isPrivate;
    @JsonProperty("is_template")
    private String isTemplate;

    private boolean getPrivate() {
        return this.isPrivate;
    }

    private void setPrivate(boolean flag) {
        this.isPrivate = flag;
    }

}
