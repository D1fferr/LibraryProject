package com.Library.UserService.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

@Component
public class JWTProvider {

    @Value("${jwt.secret}")
    private String jwtSecretKey;

    public String generatedToken(String username, UUID id, String role) {
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(120).toInstant());
        return JWT.create()
                .withSubject(id.toString())
                .withClaim("user_id", id.toString())
                .withClaim("username", username)
                .withClaim("role", role)
                .withIssuedAt(new Date())
                .withIssuer("AuthService")
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(jwtSecretKey));
    }
//    public String validateToken(String token) {
//        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(jwtSecretKey))
//                .withSubject("User details")
//                .withIssuer("AuthService")
//                .build();
//        DecodedJWT decodedJWT = jwtVerifier.verify(token);
//        return decodedJWT.getClaim("username").asString();
//    }
}
