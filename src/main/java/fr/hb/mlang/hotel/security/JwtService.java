package fr.hb.mlang.hotel.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.ValidationException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.internal.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

  @Value("${app.jwt.secret.key}")
  private String secretKey;

  @Getter
  @Value("${app.jwt.access.expiration}")
  private long jwtExpiration;

  @Getter
  @Value("${app.jwt.refresh.expiration}")
  private long refreshTokenExpiration;

  @Value("${app.jwt.verification.expiration}")
  private long verificationTokenDuration;

  public String generateVerificationToken(UserDetails userDetails) {
    return generateToken(userDetails, verificationTokenDuration, new HashMap<>());
  }

  public String generateAccessToken(UserDetails userDetails) {
    return generateToken(userDetails, jwtExpiration, new HashMap<>());
  }

  public String generateRefreshToken(UserDetails userDetails) {
    return generateToken(userDetails, refreshTokenExpiration, new HashMap<>());
  }

  public String generateToken(
      UserDetails userDetails,
      long expirationInMs,
      Map<String, Object> extraClaims
  ) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + expirationInMs);

    return Jwts
        .builder()
        .setClaims(extraClaims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .signWith(this.getSignInKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  public String extractUsernameFromToken(String token) {
    if (token == null || token.isBlank()) {
      throw new ValidationException("Token is missing or empty");
    }
    return extractClaim(token, Claims::getSubject);
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    if (token == null || token.isBlank()) {
      throw new ValidationException("Token is missing or empty");
    }
    final String username = this.extractUsernameFromToken(token);

    return (username.equals(userDetails.getUsername())) && !this.isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = this.extractAllClaims(token);

    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    try {
      return Jwts
          .parserBuilder()
          .setSigningKey(this.getSignInKey())
          .build()
          .parseClaimsJws(token)
          .getBody();
    } catch (ExpiredJwtException | SignatureException | MalformedJwtException e) {
      log.error("Failed to extract claims: {}", e.getMessage());
      throw e;
    }
  }

  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }
}
