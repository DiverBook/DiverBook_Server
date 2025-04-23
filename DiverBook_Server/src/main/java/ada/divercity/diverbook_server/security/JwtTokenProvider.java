package ada.divercity.diverbook_server.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${JWT_SECRET}")
    private String JWT_SECRET;

    private Key key;


    @PostConstruct
    public void init() {
        if (JWT_SECRET == null || JWT_SECRET.trim().isEmpty()) {
            throw new IllegalStateException("JWT_SECRET environment variable is not set");
        }
        try {
            this.key = getKey(JWT_SECRET);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("JWT_SECRET must be a valid Base64 encoded string", e);
        }
    }

    public String generateAccessToken(String userId) {
        final long EXPIRATION_TIME = 1000L * 60 * 30;

        return Jwts.builder()
                .id(userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(String userId) {
        final long EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 7;

        return Jwts.builder()
                .id(userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    private Key getKey(String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String validateAndGetUserId(String token) {
        try {
            return Jwts.parser()
                    .verifyWith((SecretKey) key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getId();
        } catch (JwtException e) {
            throw new RuntimeException("JWT token is invalid: ", e);
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

}
