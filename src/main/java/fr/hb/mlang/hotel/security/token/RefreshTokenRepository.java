package fr.hb.mlang.hotel.security.token;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

  List<RefreshToken> findAllByUserEmail(String userEmail);
}
