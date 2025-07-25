package fr.hb.mlang.hotel.auth.token;

import fr.hb.mlang.hotel.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

  Optional<RefreshToken> findByToken(String token);

  void deleteByUser(User user);
}
