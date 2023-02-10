package edu.custom.spring.security.model.entity.security;

public enum PrivilegesEnum {

    READ("READ"),
    WRITE("WRITE");

    private final String value;

    PrivilegesEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
