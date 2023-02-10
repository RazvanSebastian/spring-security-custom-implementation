package edu.custom.spring.security.model.entity.security;

public enum AuthenticationType {

    GOOGLE("google"),
    GITHUB("github"),
    BASIC("basic");

    private final String value;

    AuthenticationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
