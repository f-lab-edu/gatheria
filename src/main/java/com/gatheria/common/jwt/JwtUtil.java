package com.gatheria.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class JwtUtil {

  @Value("${jwt.secret}")
  private String secretKey;

  @Value("${jwt.expiration}")
  private long expirationTime;

  private Key getSigningKey() {
    return Keys.hmacShaKeyFor(secretKey.getBytes());
  }


  public String createStudentAccessToken(String email, String role, Long memberId, Long studentId) {
    Date now = new Date();
    Date expiration = new Date(now.getTime() + expirationTime);

    return Jwts.builder()
        .setSubject(email)
        .claim("role", role)
        .claim("memberId", memberId)
        .claim("studentId", studentId)
        .setIssuedAt(now)
        .setExpiration(expiration)
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  public String createInstructorAccessToken(String email, String role, Long memberId,
      Long instructorId) {
    Date now = new Date();
    Date expiration = new Date(now.getTime() + expirationTime);

    return Jwts.builder()
        .setSubject(email)
        .claim("role", role)
        .claim("memberId", memberId)
        .claim("instructorId", instructorId)  // 교수자 ID 추가
        .setIssuedAt(now)
        .setExpiration(expiration)
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  public boolean validateAccessToken(String token) {

    try {
      Jwts.parserBuilder()
          .setSigningKey(getSigningKey())
          .build()
          .parseClaimsJws(token);

      Claims claims = extractClaims(token);
      Date expiration = claims.getExpiration();
      return !expiration.before(new Date());
    } catch (JwtException e) {
      System.out.println("JWT 검증 실패: " + e.getMessage());
      return false;
    }

  }

  private Claims extractClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  public String extractEmail(String token) {
    return extractClaims(token).getSubject();
  }

  public String extractRole(String token) {
    return extractClaims(token).get("role", String.class);
  }

  public Long extractMemberId(String token) {
    return extractClaims(token).get("memberId", Long.class);
  }


  public Long extractStudentId(String token) {
    return extractClaims(token).get("studentId", Long.class);
  }

  public Long extractInstructorId(String token) {
    return extractClaims(token).get("instructorId", Long.class);
  }
}
