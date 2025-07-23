package fr.hb.mlang.hotel.user;

import fr.hb.mlang.hotel.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

  /**
   * Finds a {@link User} using their <code>email</code>
   *
   * @param email Email of the user we're trying to get
   * @return if found, the <code>User</code> found with this <code>email</code>.
   */
  Optional<User> findByEmail(String email);
}
