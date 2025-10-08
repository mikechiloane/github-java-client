package com.recceda.elements;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Owner {
    private String login;
    private String name;
    private String id;
    private String avatarUrl;
    private String following;
    private String followers;
}
