package com.Library.UserService.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JWTProvider {

    @Value("${jwt_secret}")
    private String jwtSecretKey;
    private final Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(120).toInstant());

    public String generatedToken(String username, String role) {
        return JWT.create()
                .withSubject("User details")
                .withClaim("username", username)
                .withClaim("role", role)
                .withIssuedAt(new Date())
                .withIssuer("AuthService")
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(jwtSecretKey));
    }
    public String validateToken(String token) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(jwtSecretKey))
                .withSubject("User details")
                .withIssuer("AuthService")
                .build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        return decodedJWT.getClaim("username").asString();
    }
}
