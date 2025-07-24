package fr.hb.mlang.hotel.auth.business;

import fr.hb.mlang.hotel.auth.AuthMapper;
import fr.hb.mlang.hotel.auth.dto.RegisterRequestDTO;
import fr.hb.mlang.hotel.auth.exception.VerifyTokenException;
import fr.hb.mlang.hotel.user.domain.Role;
import fr.hb.mlang.hotel.user.domain.User;
import fr.hb.mlang.hotel.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationManager {

  private final UserRepository userRepository;
  private final PasswordEncoder encoder;
  private final AuthMapper mapper;

  /**
   * Prepares the new {@link User} with credentials and data from the registration form and
   * pre-verification data.
   *
   * @param request DTO containing data from the registration form
   * @return the newly created User ready to be persisted
   */
  public User prepareNewUser(RegisterRequestDTO request) {
    if (userRepository.findByEmail(request.getEmail()).isPresent()) {
      throw new IllegalArgumentException("Email already in use");
    }

    User user = mapper.toUser(request);
    user.setPassword(encoder.encode(request.getPassword()));
    user.setRole(Role.USER);
    user.setValidated(false);

    return user;
  }

  /**
   * Checks that the email corresponds to an existing {@link User} and sets their account to
   * "validated".
   *
   * @param email Email of the user we want to verify
   * @return the now verified <code>User</code>
   */
  public User verifyUser(String email) {
    User user = userRepository
        .findByEmail(email)
        .orElseThrow(() -> new VerifyTokenException("Account is already verified"));

    if (user.isValidated()) {
      throw new VerifyTokenException("Account is already verified");
    }

    user.setValidated(true);
    return user;
  }
}
