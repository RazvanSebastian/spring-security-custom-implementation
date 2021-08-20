package edu.custom.spring.security.security;


import javax.servlet.http.Cookie;

public class CookiesUtils {

    private static final String JWT_COOKIE_NAME = "access_token";

    /**
     * @param accessToken - generated jwt or null on invalidating case.
     * @param isInvalid   - true: set the cookie maxAge with 0 in order to be removed from browser.
     * @return
     */
    public static Cookie createAccessTokenCookie(final String accessToken, final boolean isInvalid) {
        Cookie cookie = new Cookie(JWT_COOKIE_NAME, accessToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        if (isInvalid) {
            cookie.setMaxAge(0);
        }
        return cookie;
    }

    public static String getJwtCookieName() {
        return JWT_COOKIE_NAME;
    }

}
