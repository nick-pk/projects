package com.app.auth.service;

import com.app.auth.utill.Constant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {
    // Generate JWT token with expiration of 1 hour
    public String generateToken(String username) {
        long millis = System.currentTimeMillis();
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)  // Setting claims (currently empty, but can add custom claims)
                .setSubject(username.trim())  // Username as the subject
                .setIssuedAt(new Date(millis))  // Set issued date
                .setExpiration(new Date(millis + Constant.EXPIRATION))  // Token expires in 1 hour
                .signWith(getKey())  // Signing with the secret key
                .compact();
    }

    // Get the Secret Key for HMAC SHA256
    private SecretKey getKey() {
        try {
            // Use SHA-256 to hash the concatenated string into a fixed-length key (256 bits or 32 bytes)
            MessageDigest digest = MessageDigest.getInstance(Constant.ALGO);
            byte[] hashedBytes = digest.digest(Constant.SECRET.getBytes(StandardCharsets.UTF_8));

            // We now have a 256-bit key (32 bytes), which is secure for HMAC-SHA256
            return Keys.hmacShaKeyFor(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating HMAC key: SHA-256 algorithm not found", e);
        }
    }

    // Extract username from JWT
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract a specific claim from JWT
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    // Extract all claims from JWT
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getKey())  // Set the signing key
                .build()
                .parseClaimsJws(token)  // Parse the JWT token and get claims
                .getBody();  // Return the body (claims)
    }

    // Validate the token
    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    // Check if the token is expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract expiration date from token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
