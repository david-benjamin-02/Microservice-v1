package com.OpenApi.OpenApi;

public class RowData {
    private String pathUrl;
    private String method;
    private String functionName;
    private String summary;
    private String paramName;
    private String paramType;
    private String paramRequired;
    private String paramIn;

    // Getters and setters for the properties
    public String getPathUrl() {
        return pathUrl;
    }

    public void setPathUrl(String pathUrl) {
        this.pathUrl = pathUrl;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getParamRequired() {
        return paramRequired;
    }

    public void setParamRequired(String paramRequired) {
        this.paramRequired = paramRequired;
    }

    public String getParamIn() {
        return paramIn;
    }

    public void setParamIn(String paramIn) {
        this.paramIn = paramIn;
    }
}

