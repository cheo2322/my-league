package com.deveclopers.myleague.security;

import com.deveclopers.myleague.document.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtService {

  private final SecretKey secretKey;
  private final long expirationMillis;

  public JwtService(
      @Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long expirationMillis) {

    this.expirationMillis = expirationMillis;
    this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  public String generateToken(User user) {
    return Jwts.builder()
        .setSubject(user.getEmail())
        .claim("role", user.getGlobalRole().name())
        .claim("userId", user.getId())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
        .signWith(secretKey, SignatureAlgorithm.HS256)
        .compact();
  }

  public String extractUserId(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(secretKey)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .get("userId", String.class);
  }
}
