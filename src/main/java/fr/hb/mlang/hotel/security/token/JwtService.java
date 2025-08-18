package fr.hb.mlang.hotel.security.token;

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

  @Value("${app.jwt.password.expiration}")
  private long resetTokenExpiration;

  /**
   * Generates a token used in the verification email after registration.
   *
   * @param userDetails Details of the user
   * @return the generated verification token.
   */
  public String generateVerificationToken(UserDetails userDetails) {
    return generateToken(userDetails, verificationTokenDuration, new HashMap<>());
  }

  /**
   * Generates an <code>access token</code> (JWT).
   *
   * @param userDetails Details of the user
   * @return the generated access token.
   */
  public String generateAccessToken(UserDetails userDetails) {
    return generateToken(userDetails, jwtExpiration, new HashMap<>());
  }

  /**
   * Generates a <code>refresh token</code> (JWT).
   *
   * @param userDetails Details of the user
   * @return the generated refresh token.
   */
  public String generateRefreshToken(UserDetails userDetails) {
    return generateToken(userDetails, refreshTokenExpiration, new HashMap<>());
  }

  /**
   * Generates a <code>token</code> for a password reset link.
   *
   * @param userDetails Details of the user
   * @return the generated password reset token.
   */
  public String generateResetPasswordToken(UserDetails userDetails) {
    return generateToken(userDetails, resetTokenExpiration, new HashMap<>());
  }

  /**
   * Generic method that generates a new JWT.
   *
   * @param userDetails    Information we will need to identify the User to the token (ex:
   *                       username)
   * @param expirationInMs Expiration time in milliseconds
   * @param extraClaims    Array of additional claims (ex: roles) // Future uses
   * @return a newly generated token.
   */
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

  /**
   * Checks the validity of a token by verifying both if the username corresponds to a user and if
   * the token is expired.
   *
   * @param token       Token we're trying to verify
   * @param userDetails User that the token should belong to
   * @return {true} if the token is valid, {false} if not.
   */
  public boolean isTokenValid(String token, UserDetails userDetails) {
    if (token == null || token.isBlank()) {
      throw new ValidationException("Token is missing or empty");
    }
    final String username = this.extractUsernameFromToken(token);

    return (username.equals(userDetails.getUsername())) && !this.isTokenExpired(token);
  }

  /**
   * Checks if the token is expired by comparing its expiration date and the current date.
   *
   * @param token Token we want to check
   * @return {true} if the token is expired, {false} if the token is still active.
   */
  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  /**
   * Extracts the <code>Username</code> (claim) from a given token.
   *
   * @param token Jwt
   * @return the <code>username</code> found in the token.
   */
  public String extractUsernameFromToken(String token) {
    if (token == null || token.isBlank()) {
      throw new ValidationException("Token is missing or empty");
    }
    return extractClaim(token, Claims::getSubject);
  }

  /**
   * Extracts the <code>Expiration date</code> (claim) from a given token.
   *
   * @param token Jwt
   * @return the <code>expiration date</code> of the token.
   */
  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  /**
   * Generic method that extracts a specific <code>claim</code> from a token.
   *
   * @param token          Token we want to extract a claim from
   * @param claimsResolver Method used to extract the claim
   * @param <T>            Type of the claim to extract
   * @return the extracted <code>claim</code>.
   */
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = this.extractAllClaims(token);

    return claimsResolver.apply(claims);
  }

  /**
   * Extracts the <code>Claims</code> (information such as email, expiration date, roles, etc.) from
   * the token.
   *
   * @param token Token we want to extract the claims from.
   * @return a <code>Claim</code> object from the given token.
   */
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

  /**
   * Decodes our <code>Secret Key</code> (also called "Sign-in key") used to create tokens and
   * extract claims.
   *
   * @return our decoded secret key.
   */
  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
