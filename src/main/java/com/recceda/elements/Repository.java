package com.recceda.elements;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Repository {
    private String id;
    private String nodeId;
    private String fullName;
    private String isPrivate;
    private String owner;
}
