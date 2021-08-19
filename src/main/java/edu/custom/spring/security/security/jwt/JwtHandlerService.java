package edu.custom.spring.security.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class JwtHandlerService {
    private static final String HEADER_NAME_TYPE = "typ";
    private static final String HEADER_TYP_VALUE = "JWT";

    private static final String PRINCIPAL_CLAIM_NAME = "principal";
    private static final String ROLES_CLAIM_NAME = "roles";

    private final JwtProperties jwtProperties;

    public JwtHandlerService(final JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String generateAccessToken(final Authentication authentication) {
        final Long dateNowMillis = System.currentTimeMillis();
        final Date issuedAt = new Date(dateNowMillis);
        final Date availability = new Date(dateNowMillis + jwtProperties.getAvailability());
        return Jwts.builder()
                .setHeaderParam(HEADER_NAME_TYPE, HEADER_TYP_VALUE)
                .setClaims(buildClaims(authentication))
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(issuedAt)
                .setExpiration(availability)
                .setId(String.valueOf(UUID.randomUUID()))
                .signWith(SignatureAlgorithm.RS256, jwtProperties.getPrivateKey())
                .compact();
    }

    public Authentication decodeAccessToken(final String accessToken) {
        final Jws<Claims> claims = Jwts.parser()
                .setSigningKey(jwtProperties.getPublicKey())
                .parseClaimsJws(accessToken);
        final String principal = claims.getBody().get(PRINCIPAL_CLAIM_NAME, String.class);
        final Collection<GrantedAuthority> authorities = extractAuthorities(claims);
        return new UsernamePasswordAuthenticationToken(principal, null, authorities);
    }

    private Map<String, Object> buildClaims(final Authentication authentication) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(PRINCIPAL_CLAIM_NAME, authentication.getPrincipal());
        claims.put(ROLES_CLAIM_NAME, authentication.getAuthorities());
        return claims;
    }

    private Collection<GrantedAuthority> extractAuthorities(Jws<Claims> claims) {
        final List<LinkedHashMap<String, ?>> authorities = (List<LinkedHashMap<String, ?>>) claims.getBody().get(ROLES_CLAIM_NAME);
        return authorities.stream()
                .map(roleMap -> new SimpleGrantedAuthority((String) roleMap.get("authority")))
                .collect(Collectors.toList());
    }

}
