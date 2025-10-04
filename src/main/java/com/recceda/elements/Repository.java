package com.recceda.elements;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Setter
public class Repository {
    private String id;
    private String nodeId;
    private String fullName;
    private String isPrivate;
    private Owner owner;
}
