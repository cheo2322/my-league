package com.deveclopers.myleague.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
  private static final long EXPIRATION_TIME = 86400000;
  private final SecretKey key;

  public JwtUtil(@Value("${TOKEN_SECRET_KEY}") String secretKey) {
    this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
  }

  public String generateToken(String username, String role) {
    return Jwts.builder()
        .subject(username)
        .claim("role", role)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .signWith(key)
        .compact();
  }

  public String validateToken(String token) {
    try {
      return Jwts.parser()
          .verifyWith(key)
          .build()
          .parseSignedClaims(token)
          .getPayload()
          .getSubject();
    } catch (JwtException e) {
      return null;
    }
  }

  public String getRoleFromToken(String token) {
    try {
      Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();

      return claims.get("role", String.class);
    } catch (Exception e) {
      return null;
    }
  }
}
