package fr.hb.mlang.hotel.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import fr.hb.mlang.hotel.auth.token.RefreshToken;
import fr.hb.mlang.hotel.auth.token.RefreshTokenRepository;
import fr.hb.mlang.hotel.user.domain.User;
import fr.hb.mlang.hotel.user.security.CustomUserDetails;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Lettuce.Cluster.Refresh;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtProvider {

  private final JwtKeyManager jwtKeyManager;
  private final UserDetailsService userDetailsService;
  private final RefreshTokenRepository refreshTokenRepository;

  /**
   * Creates an <code>access token</code> that will be valid for <code>5 minutes</code>.
   *
   * @param email Email of the user who will get the token
   * @return the new <code>access token</code>
   * @implNote <code>Access token</code> common duration: 5-15 minutes
   */
  public String createAccessToken(String email) {
    return this.generateToken(email, Instant.now().plus(15, ChronoUnit.MINUTES));
  }

  /**
   * Creates a <code>refresh token</code> that will be valid for <code>7 days</code>.
   *
   * @param userDetails User who will get the token
   * @return the new <code>refresh token</code>
   * @implNote <code>Refresh token</code> common duration: 7-30 days
   */
  public RefreshToken createRefreshToken(CustomUserDetails userDetails) {
    Instant expiresAt = Instant.now().plus(7, ChronoUnit.DAYS);
    String token = UUID.randomUUID().toString();
    //String token = this.generateToken(user.getEmail(), expiresAt);

    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setToken(token);
    refreshToken.setExpiresAt(expiresAt);
    refreshToken.setUser(userDetails.getUser());

    return refreshToken;
  }

  /**
   * Generates a <code>JWT</code> using the {@link User}'s email.
   *
   * @param email           Email of the user who will get the token
   * @param tokenExpiration Expiration time and date of the token
   * @return the generate <code>JWT</code>
   */
  public String generateToken(String email, Instant tokenExpiration) {
    return JWT
        .create()
        .withSubject(email)
        //.withClaim("roles", <array-of-roles-here>) //When setting up role-based routes
        .withIssuedAt(Instant.now())
        .withExpiresAt(tokenExpiration)
        .sign(jwtKeyManager.getAlgorithm());
  }

  /**
   * Decodes the <code>token</code> and extracts the {@link User}'s email.
   *
   * @param token Token that identifies the user
   * @return the decoded email address
   */
  public String extractEmailFromToken(String token) {
    try {
      DecodedJWT decodedJWT = JWT.require(jwtKeyManager.getAlgorithm()).build().verify(token);
      return decodedJWT.getSubject();
    } catch (JWTVerificationException e) {
      throw new AuthorizationDeniedException("Invalid token" + e.getMessage());
    }
  }

  /**
   * Checks if a <code>JWT</code> is valid and corresponds to a {@link User}.
   *
   * @param token String token to check
   * @return the <code>User</code> associated to the token
   */
  public UserDetails verifyAccessToken(String token) {
    try {
      String userIdentifier = this.extractEmailFromToken(token);

      return userDetailsService.loadUserByUsername(userIdentifier);
    } catch (JWTVerificationException | UsernameNotFoundException e) {
      throw new AuthorizationDeniedException("Invalid token: " + e.getMessage());
    }
  }

  /**
   * Verifies that the <code>token</code> corresponds to an existing non-expired
   * {@link RefreshToken} and returns the corresponding {@link User}.
   *
   * @param token Value of the token we want to verify
   * @return the <code>User</code> who owns the <code>RefreshToken</code>
   */
  public User verifyRefreshToken(String token) {
    RefreshToken refreshToken = refreshTokenRepository
        .findByToken(token)
        .orElseThrow(() -> new AuthorizationDeniedException("Invalid refresh token"));

    if (refreshToken.isExpired()) {
      refreshTokenRepository.delete(refreshToken);
      throw new AuthorizationDeniedException("Refresh token expired. Please login.");
    }

    return refreshToken.getUser();
  }
}
