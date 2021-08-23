package edu.custom.spring.security.security;


import javax.servlet.http.Cookie;

public class SecurityUtils {

    public static final String JWT_COOKIE_NAME = "access_token";
    public static final String CSRF_HEADER_NAME = "CSRF_TOKEN";
    public static final String CSRF_JWT_CLAIM_HEADER_NAME = "CSRF_TOKEN_CLAIM";

    public static Cookie createAccessTokenCookie(final String accessToken, final boolean isInvalid) {
        Cookie cookie = new Cookie(JWT_COOKIE_NAME, accessToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        if (isInvalid) {
            cookie.setMaxAge(0);
        }
        return cookie;
    }

}
