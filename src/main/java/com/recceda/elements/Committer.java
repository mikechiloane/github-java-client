package com.recceda.elements;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class Committer {
    private String name;
    private String email;
}
