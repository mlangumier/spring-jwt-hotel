package fr.hb.mlang.hotel.auth;

import fr.hb.mlang.hotel.email.EmailDetails;
import fr.hb.mlang.hotel.email.EmailService;
import fr.hb.mlang.hotel.enums.Role;
import fr.hb.mlang.hotel.user.User;
import fr.hb.mlang.hotel.user.UserRepository;
import jakarta.persistence.EntityExistsException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

  private UserRepository userRepository;
  private EmailService emailService;
  private PasswordEncoder encoder;

  @Override
  public User register(User user) {
    if (userRepository.findByEmail(user.getEmail()).isPresent()) {
      throw new EntityExistsException("User already exists");
    }

    String serverUrl = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();

    String messageBody = """
        Hello and welcome! Click <a href="%s">here</a> to validate your account.
        """.formatted(serverUrl + "/api/v1/auth/validate/" + user.getEmail());

    user.setPassword(encoder.encode(user.getPassword()));
    user.setRole(Role.USER);
    user.setValidated(false);

    userRepository.save(user);
    emailService.sendEmail(
        new EmailDetails(user.getEmail(), "JWT Hotel - Validate your account", messageBody, null));

    return user;
  }

  @Override
  public void validateUser(User user) {
    //TODO: Check validation email & update user.validated to true
  }

  @Override
  public void resetPassword(String email) {
    //TODO:
  }

  @Override
  public void deleteAccount(User user) {
    //TODO
  }
}
