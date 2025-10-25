package library.com.apigateway.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    @Value("${}")
    private String jwtSecret;
    private final JwtParser jwtParser;

    public JwtUtil(@Value("${}") String jwtSecret){
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
            return jwtParser.parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            throw new JwtException("JWT token expired", e);
        } catch (UnsupportedJwtException e) {
            throw new JwtException("Unsupported JWT token", e);
        } catch (MalformedJwtException e) {
            throw new JwtException("Invalid JWT token format", e);
        } catch (SecurityException e) {
            throw new JwtException("JWT signature validation failed", e);
        } catch (IllegalArgumentException e) {
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
