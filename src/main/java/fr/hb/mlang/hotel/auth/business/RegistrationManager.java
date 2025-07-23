package fr.hb.mlang.hotel.auth.business;

import fr.hb.mlang.hotel.auth.dto.AuthMapper;
import fr.hb.mlang.hotel.auth.dto.RegisterRequestDTO;
import fr.hb.mlang.hotel.enums.Role;
import fr.hb.mlang.hotel.user.User;
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

  public User prepareNewUser(RegisterRequestDTO request) {
    if (userRepository.findByEmail(request.email()).isPresent()) {
      throw new IllegalArgumentException("Email already in use");
    }

    User user = mapper.toUser(request);
    user.setPassword(encoder.encode(request.password()));
    user.setRole(Role.USER);
    user.setValidated(false);

    return user;
  }
}
