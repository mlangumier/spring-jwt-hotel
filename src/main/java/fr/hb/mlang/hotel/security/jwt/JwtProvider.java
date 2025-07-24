package fr.hb.mlang.hotel.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import fr.hb.mlang.hotel.user.domain.User;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtProvider {

  private final UserDetailsService userDetailsService;
  private final JwtKeyManager jwtKeyManager;

  /**
   * Generates a <code>JWT</code> containing the {@link User}'s email.
   *
   * @param email Email of the user who will get the token
   * @return the generated <code>JWT</code>
   */
  public String generateToken(String email) {
    return JWT.create()
        .withSubject(email)
        .withExpiresAt(Instant.now().plus(15, ChronoUnit.MINUTES))
        .sign(jwtKeyManager.getAlgorithm());
  }

  /**
   * Generates a <code>JWT</code> containing the {@link User}'s email.
   *
   * @param email           Email of the user who will get the token
   * @param tokenExpiration Expiration time and date of the token
   * @return the generated <code>JWT</code>
   */
  public String generateToken(String email, Instant tokenExpiration) {
    return JWT
        .create()
        .withSubject(email)
        .withExpiresAt(tokenExpiration)
        .sign(jwtKeyManager.getAlgorithm());
  }

  /**
   * Decodes the <code>token</code> and extracts the {@link User}'s email.
   *
   * @param token Token that identifies the user
   * @return the decoded email address
   */
  public String extractEmail(String token) {
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
  public UserDetails verifyToken(String token) {
    try {
      String userIdentifier = this.extractEmail(token);
      return userDetailsService.loadUserByUsername(userIdentifier);
    } catch (JWTVerificationException | UsernameNotFoundException e) {
      throw new AuthorizationDeniedException("Invalid token: " + e.getMessage());
    }
  }
}
