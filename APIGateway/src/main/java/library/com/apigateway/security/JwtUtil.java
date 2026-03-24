package library.com.apigateway.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class JwtUtil {


    private String jwtSecret;
    private final JwtParser jwtParser;

    public JwtUtil(@Value("${jwt.secret}") String jwtSecret){
        this.jwtSecret=jwtSecret;
        this.jwtParser=Jwts.parser()
                .setSigningKey(getSigningKey())
                .build();
    }

    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public Claims extractAllClaims(String token) throws JwtException {
        try {
            log.info("Trying to extract all claims from token");
            return jwtParser.parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            log.info("JWT token expired");
            throw new JwtException("JWT token expired", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token");
            throw new JwtException("Unsupported JWT token", e);
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token format");
            throw new JwtException("Invalid JWT token format", e);
        } catch (SecurityException e) {
            log.info("JWT signature validation failed");
            throw new JwtException("JWT signature validation failed", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token is empty or null");
            throw new JwtException("JWT token is empty or null", e);
        }
    }
    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token){
        Claims claims = extractAllClaims(token);
        return claims.get("roles", List.class);
    }
    public Date extractExpirationDate(String token){
        return extractAllClaims(token).getExpiration();
    }
    public boolean isTokenExpired(String token){
        try {
            return extractExpirationDate(token).before(new Date());
        }catch (JwtException e){
            return true;
        }
    }
    public boolean validateToken(String token){
        try {
            extractAllClaims(token);
            return !isTokenExpired(token);
        }catch (JwtException | IllegalArgumentException e){
            return false;
        }
    }

}
