package edu.custom.spring.security.model.entity.security;

public enum RolesEnum {

    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String value;

    RolesEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
