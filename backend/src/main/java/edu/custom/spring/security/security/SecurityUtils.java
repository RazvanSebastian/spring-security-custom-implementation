package edu.custom.spring.security.security;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class SecurityUtils {

    public static final String JWT_COOKIE_NAME = "access_token";

    public static void addAccessTokenToCookies(final HttpServletResponse response, final String accessToken) {
        Cookie cookie = new Cookie(JWT_COOKIE_NAME, accessToken);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }

    public static void removeAccessTokenFromCookies(final HttpServletResponse response) {
        Cookie cookie = new Cookie(JWT_COOKIE_NAME, null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

}
