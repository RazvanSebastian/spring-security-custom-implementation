package edu.custom.spring.security.security;

public class CookiesUtils {

    private static final String JWT_COOKIE_NAME = "access_token";

    public static String getJwtCookieName() {
        return JWT_COOKIE_NAME;
    }
}
