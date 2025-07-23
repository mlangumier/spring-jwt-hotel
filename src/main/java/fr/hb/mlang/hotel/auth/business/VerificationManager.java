package fr.hb.mlang.hotel.auth.business;

import fr.hb.mlang.hotel.auth.exception.VerifyTokenException;
import fr.hb.mlang.hotel.user.User;
import fr.hb.mlang.hotel.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationManager {

  private final UserRepository userRepository;

  public User verifyUser(String email) {
    User user = userRepository.findByEmail(email).orElseThrow(() -> new VerifyTokenException("Account already verified"));

    if (user.isValidated()) {
      throw new VerifyTokenException("User is already verified");
    }

    user.setValidated(true);
    return user;
  }
}
