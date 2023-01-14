package edu.custom.spring.security.security.jwt;

import lombok.Getter;
import lombok.Setter;

import java.security.PrivateKey;
import java.security.PublicKey;

@Getter
@Setter
public class JwtProperties {

    private PublicKey publicKey;
    private PrivateKey privateKey;
    private String issuer;
    private Long availability;
}
