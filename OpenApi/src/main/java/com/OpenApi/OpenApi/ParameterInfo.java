package com.OpenApi.OpenApi;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class ParameterInfo {
    private String name;
    private Boolean required;
    private String parameterIn; // Rename "in" to avoid conflict with SQL keyword
    private String type;

    public ParameterInfo(String name, Boolean required, String parameterIn, String type) {
        this.name = name;
        this.required = required;
        this.parameterIn = parameterIn;
        this.type = type;
    }

    public ParameterInfo() {

    }
}
