package com.recceda.elements;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class Repository {
    private String id;
    private String nodeId;
    private String name;
    private String fullName;
    private boolean isPrivate;
    private Owner owner;

    public void setPrivate(boolean isPrivate){
        this.isPrivate = isPrivate;
    }

}
