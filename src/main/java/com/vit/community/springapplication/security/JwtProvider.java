package com.vit.community.springapplication.security;

import com.vit.community.springapplication.exceptions.SpringCommunityException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.sql.Date;
import java.time.Instant;

import static io.jsonwebtoken.Jwts.parser;
import static java.util.Date.from;

/*
* This class is responsible for creating as well as validating the JSON Web Token.
*
* We are using AsymmetricEncryption to sign our JWT’s using Java Keystore (using Public-Private Key).
* */

@Service
public class JwtProvider {

    private KeyStore keyStore;
    @Value("${jwt.expiration.time}")
    private Long jwtExpirationInMillis;

    /*
    * Once the JwtProvider instance is loaded, the init() function loads the Java keystore
    * for signing the JWTs, and throws an exception if it fails to do so.
    * */

    @PostConstruct
    public void init() {
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream("/springblog.jks");
            keyStore.load(resourceAsStream, "secret".toCharArray());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new SpringCommunityException("Exception occurred while loading keystore", e);
        }

    }

    /*
    * Once the user trying to log in gets validated, this function receives the Authentication
    * object generated by the AuthService class and generates a JWT from the Jwts class for
    * the user and signs it with a private (secret) key.
    * */

    public String generateToken(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(principal.getUsername())
                .setIssuedAt(from(Instant.now()))
                .signWith(getPrivateKey())
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
                .compact();
    }

    public String generateTokenWithUserName(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(from(Instant.now()))
                .signWith(getPrivateKey())
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
                .compact();
    }

    /*
    * This function retrieves the private (secret) key from the Java KeyStore and throws an exception if it occurs.
    * */

    private PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey("springblog", "secret".toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new SpringCommunityException("Exception occurred while retrieving private key from keystore", e);
        }
    }

    /*
    * The validateToken method uses the JwtParser class to validate the token.
    * Since the JWT was created and signed by a Private Key, we use the
    * corresponding Public Key, to validate the token.
    * */

    public boolean validateToken(String jwt) {
        parser().setSigningKey(getPublicKey()).parseClaimsJws(jwt);
        return true;
    }

    /*
    * This function retrieves the public key from the Java keyStore and returns it
    * for validating the JWT token received from client.
    * */

    private PublicKey getPublicKey() {
        try {
            return keyStore.getCertificate("springblog").getPublicKey();
        } catch (KeyStoreException e) {
            throw new SpringCommunityException("Exception occured while " +
                    "retrieving public key from keystore", e);
        }
    }

    /*
    * This function uses the JwtParser class to retrieve the username
    * associated with that token.
    * */

    public String getUsernameFromJwt(String token) {
        Claims claims = parser()
                .setSigningKey(getPublicKey())
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public Long getJwtExpirationInMillis() {
        return jwtExpirationInMillis;
    }
}
