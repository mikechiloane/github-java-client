package com.recceda.http.requests.repository;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateRepositoryRequest {
    private String name;
    private String description;
    private String homepage;
    @JsonProperty("private")
    private boolean isPrivate;
    @JsonProperty("is_template")
    private boolean isTemplate;

    private boolean getPrivate() {
        return this.isPrivate;
    }

    private void setPrivate(boolean flag) {
        this.isPrivate = flag;
    }

}
