package fr.hb.mlang.hotel.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import fr.hb.mlang.hotel.user.domain.User;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
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
   * Checks if a <code>JWT</code> is valid and corresponds to a {@link User}.
   * @param token String token to check
   * @return the <code>User</code> associated to the token
   */
  public UserDetails validateToken(String token) {
    System.err.println(">> Token: " + token);

    try {
      DecodedJWT decodedJWT = JWT.require(jwtKeyManager.getAlgorithm()).build().verify(token);
      String userIdentifier = decodedJWT.getSubject();
      System.err.println(">> JWT: " + userIdentifier);

      return userDetailsService.loadUserByUsername(userIdentifier);
    } catch (JWTVerificationException | UsernameNotFoundException e) {
      throw new RuntimeException("Couldn't decode JWT: " + e.getMessage());
    }
  }

  /**
   * Generates a <code>JWT</code> using the {@link User}'s email.
   *
   * @param email Email of the user who will get the token
   * @return the generated <code>JWT</code>
   */
  public String generateToken(String email) {
    return JWT.create()
        .withSubject(email)
        .withExpiresAt(Instant.now().plus(30, ChronoUnit.MINUTES))
        .sign(jwtKeyManager.getAlgorithm());
  }
}
