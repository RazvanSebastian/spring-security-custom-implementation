package edu.custom.spring.security.security.jwt;

import edu.custom.spring.security.security.jwt.models.JwtProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class JwtHandlerConfiguration {

    private String privateKey;
    private String publicKey;
    private Long availability;
    private String issuer;

    public JwtHandlerConfiguration(
            @Value("${jwt.key.private}") String privateKey,
            @Value("${jwt.key.public}") String publicKey,
            @Value("${jwt.availability}") Long availability,
            @Value("${jwt.issuer}") String issuer) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.availability = availability;
        this.issuer = issuer;
    }

    @Bean
    public JwtProperties jwtProperties() throws NoSuchAlgorithmException, InvalidKeySpecException {
        JwtProperties jwtProperties = new JwtProperties();
        jwtProperties.setAvailability(availability);
        jwtProperties.setIssuer(issuer);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        jwtProperties.setPrivateKey(generatePrivateKey(keyFactory));
        jwtProperties.setPublicKey(generatePublicKey(keyFactory));
        return jwtProperties;
    }

    private PrivateKey generatePrivateKey(KeyFactory keyFactory) throws InvalidKeySpecException {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
        return keyFactory.generatePrivate(keySpec);
    }

    private PublicKey generatePublicKey(KeyFactory keyFactory) throws InvalidKeySpecException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));
        return keyFactory.generatePublic(keySpec);
    }

}
