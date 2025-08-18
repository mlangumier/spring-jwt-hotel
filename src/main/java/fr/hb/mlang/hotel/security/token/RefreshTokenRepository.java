package fr.hb.mlang.hotel.security.token;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

  /**
   * Find the {@link RefreshToken} entity that corresponds to the given JWT.
   *
   * @param token JWT that represents the refresh token
   * @return the Refresh token if found
   */
  Optional<RefreshToken> findByToken(String token);
}
